package svenhjol.charm.feature.atlases.common;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import svenhjol.charm.api.enums.EventResult;
import svenhjol.charm.api.enums.ItemStackResult;
import svenhjol.charm.feature.atlases.Atlases;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class Handlers extends FeatureHolder<Atlases> {
    public Handlers(Atlases feature) {
        super(feature);
    }

    public ItemStackResult useAtlasInHand(Level level, Player player, InteractionHand hand) {
        var held = player.getItemInHand(hand);
        if (level.isClientSide) {
            return ItemStackResult.consume(held);
        }

        if (hand == InteractionHand.OFF_HAND && !Atlases.offHandOpen) {
            return ItemStackResult.pass(held);
        }

        var inventory = AtlasInventory.get(level, held);
        inventory.getCurrentDimensionMapInfos(level).values().forEach(mapInfo
            -> feature().networking.sendMapToClient((ServerPlayer)player, mapInfo.map, true));

        player.openMenu(inventory);

        return ItemStackResult.consume(held);
    }

    public EventResult useAtlasOn(UseOnContext context) {
        var level = context.getLevel();
        var blockstate = level.getBlockState(context.getClickedPos());

        if (blockstate.is(BlockTags.BANNERS)) {
            if (!level.isClientSide && context.getPlayer() instanceof ServerPlayer) {
                var inventory = AtlasInventory.get(level, context.getItemInHand());
                var mapdata = inventory.getActiveMap(level);

                if (mapdata != null) {
                    mapdata.toggleBanner(level, context.getClickedPos());
                }
            }

            return EventResult.SUCCESS;
        }

        return EventResult.PASS;
    }

    /**
     * Callback from {@link MapItemSavedDataMixin} to check
     * if player is holding a map or a player is holding an atlas that contains a map.
     * @param inventory Inventory to check.
     * @param predicate Check?.
     * @return True if the player has a map or the player has an atlas that has a map.
     */
    public boolean doesAtlasContainMap(Inventory inventory, Predicate<ItemStack> predicate) {
        if (inventory.contains(predicate)) {
            return true;
        }

        for (var hand : InteractionHand.values()) {
            var atlasStack = inventory.player.getItemInHand(hand);
            if (atlasStack.getItem() == feature().registers.item.get()) {
                var inv = AtlasInventory.get(inventory.player.level(), atlasStack);
                if (inv.matches(predicate)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void setupAtlasUpscale(Inventory playerInventory, CartographyTableMenu container) {
        var oldSlot = container.slots.get(0);
        container.slots.set(0, new Slot(oldSlot.container, oldSlot.index, oldSlot.x, oldSlot.y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return oldSlot.mayPlace(stack) || stack.getItem() == feature().registers.item.get()
                    && AtlasInventory.get(playerInventory.player.level(), stack).getMapInfos().isEmpty();
            }
        });
    }

    public boolean makeAtlasUpscaleOutput(ItemStack topStack, ItemStack bottomStack, ItemStack outputStack, Level level,
                                                 ResultContainer craftResultInventory, CartographyTableMenu cartographyContainer) {
        if (topStack.getItem() == feature().registers.item.get()) {
            ItemStack output;
            var inventory = AtlasInventory.get(level, topStack);

            if (inventory.getMapInfos().isEmpty() && bottomStack.getItem() == Items.MAP && inventory.getScale() < 4) {
                output = topStack.copy();

                AtlasData.getMutable(output)
                    .setId(UUID.randomUUID())
                    .setScale(inventory.getScale() + 1)
                    .save(output);
            } else {
                output = ItemStack.EMPTY;
            }

            if (!ItemStack.matches(output, outputStack)) {
                craftResultInventory.setItem(2, output);
                cartographyContainer.broadcastChanges();
            }

            return true;
        }

        return false;
    }

    public void swappedSlot(Player player, Networking.C2SSwapAtlasSlot request) {
        var swappedSlot = request.slot();
        var offhandItem = player.getOffhandItem().copy();
        var mainHandItem = player.getMainHandItem().copy();
        var inventory = player.getInventory();

        Consumer<Integer> doSwap = i -> {
            var swap = inventory.getItem(i).copy();
            inventory.setItem(i, mainHandItem);
            player.setItemInHand(InteractionHand.MAIN_HAND, swap);
            Networking.S2CSwappedAtlasSlot.send((ServerPlayer)player, i);
        };

        if (mainHandItem.getItem() instanceof Item) {
            if (swappedSlot >= 0) {
                doSwap.accept(swappedSlot);
                return;
            }
        }

        if (offhandItem.getItem() instanceof Item) return;

        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack inv = inventory.getItem(i);
            if (inv.getItem() instanceof Item) {
                slot = i;
                break;
            }
        }

        if (slot == -1) return;
        doSwap.accept(slot);
    }

    public void transferAtlas(Player player, Networking.C2STransferAtlas request) {
        var serverPlayer = (ServerPlayer)player;
        var atlasSlot = request.atlasSlot();
        var mapX = request.mapX();
        var mapZ = request.mapZ();
        var inventory = AtlasInventory.get(serverPlayer.level(), serverPlayer.getInventory().getItem(atlasSlot));

        switch (request.moveMode()) {
            case TO_HAND -> {
                serverPlayer.containerMenu.setCarried(inventory.removeMapByCoords(serverPlayer.level(), mapX, mapZ).map);
                Networking.S2CUpdateInventory.send(serverPlayer, atlasSlot);
            }
            case TO_INVENTORY -> {
                serverPlayer.addItem(inventory.removeMapByCoords(serverPlayer.level(), mapX, mapZ).map);
                Networking.S2CUpdateInventory.send(serverPlayer, atlasSlot);
            }
            case FROM_HAND -> {
                ItemStack stack = serverPlayer.containerMenu.getCarried();
                if (stack.getItem() == Items.FILLED_MAP) {
                    var mapState = MapItem.getSavedData(stack, serverPlayer.level());
                    if (mapState != null && mapState.scale == inventory.getScale()) {
                        inventory.addToInventory(serverPlayer.level(), stack);
                        serverPlayer.containerMenu.setCarried(ItemStack.EMPTY);
                        Networking.S2CUpdateInventory.send(serverPlayer, atlasSlot);
                    }
                }
            }
            case FROM_INVENTORY -> {
                ItemStack stack = serverPlayer.getInventory().getItem(mapX);
                if (stack.getItem() == Items.FILLED_MAP) {
                    var mapState = MapItem.getSavedData(stack, serverPlayer.level());
                    if (mapState != null && mapState.scale == inventory.getScale()) {
                        inventory.addToInventory(serverPlayer.level(), stack);
                        serverPlayer.getInventory().removeItemNoUpdate(mapX);
                        Networking.S2CUpdateInventory.send(serverPlayer, atlasSlot);
                    }
                }
            }
        }
    }

    public void playerTick(Player player) {
        if (!player.level().isClientSide) {
            var serverPlayer = (ServerPlayer) player;

            for (var hand : InteractionHand.values()) {
                var held = serverPlayer.getItemInHand(hand);

                if (held.getItem() == feature().registers.item.get()) {
                    var inventory = AtlasInventory.get(serverPlayer.level(), held);
                    if (inventory.updateActiveMap(serverPlayer)) {
                        var slot = getSlotFromHand(serverPlayer, hand);
                        Networking.S2CUpdateInventory.send(serverPlayer, slot);

                        if (inventory.getMapInfos().size() >= Advancements.NUMBER_OF_MAPS_FOR_ACHIEVEMENT) {
                            feature().advancements.madeAtlasMaps(serverPlayer);
                        }
                    }
                }
            }
        }
    }

    public int getSlotFromHand(Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return player.getInventory().selected;
        } else {
            return player.getInventory().getContainerSize() - 1;
        }
    }

    public void playerLogin(Player player) {
        if (!player.level().isClientSide()) {
            Networking.S2CSwappedAtlasSlot.send((ServerPlayer)player, -1);
        }
    }
}
