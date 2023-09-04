package svenhjol.charm.feature.atlases;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.atlases.MapItemSavedDataMixin;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.api.event.PlayerLoginEvent;
import svenhjol.charmony.api.event.PlayerTickEvent;
import svenhjol.charmony.api.iface.IWandererTradeProvider;
import svenhjol.charmony.api.iface.IWandererTrade;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.AdvancementHelper;
import svenhjol.charmony.helper.ItemNbtHelper;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Feature(mod = Charm.MOD_ID, description = "Storage for maps that automatically updates the displayed map as you explore.")
public class Atlases extends CharmFeature implements IWandererTradeProvider {
    public static Supplier<Item> ITEM;
    public static Supplier<MenuType<AtlasContainer>> MENU_TYPE;
    public static Supplier<SoundEvent> OPEN_SOUND;
    public static Supplier<SoundEvent> CLOSE_SOUND;
    private static final int NUMBER_OF_MAPS_FOR_ACHIEVEMENT = 10;

    @Configurable(name = "Open in off hand", description = "Allow opening the atlas while it is in the off-hand.")
    public static boolean offHandOpen = false;

    @Configurable(name = "Map scale", description = "Map scale used in atlases by default.")
    public static int defaultScale = 0;

    @Override
    public void register() {
        var registry = Charm.instance().registry();

        ITEM = registry.item("atlas", () -> new AtlasItem(this));
        MENU_TYPE = registry.menuType("atlas", () -> new MenuType<>(AtlasContainer::new, FeatureFlags.VANILLA_SET));
        OPEN_SOUND = registry.soundEvent("atlas_open");
        CLOSE_SOUND = registry.soundEvent("atlas_close");

        AtlasesNetwork.register();
        CharmonyApi.registerProvider(this);
    }

    @Override
    public void runWhenEnabled() {
        PlayerLoginEvent.INSTANCE.handle(this::handlePlayerLogin);
        PlayerTickEvent.INSTANCE.handle(this::handlePlayerTick);
    }

    public static void handleTransferAtlas(AtlasesNetwork.TransferAtlas request, Player player) {
        var atlasSlot = request.getAtlasSlot();
        var mapX = request.getMapX();
        var mapZ = request.getMapZ();
        var inventory = AtlasInventory.get(player.level(), player.getInventory().getItem(atlasSlot));

        switch (request.getMoveMode()) {
            case TO_HAND -> {
                player.containerMenu.setCarried(inventory.removeMapByCoords(player.level(), mapX, mapZ).map);
                AtlasesNetwork.UpdateInventory.send(player, atlasSlot);
            }
            case TO_INVENTORY -> {
                player.addItem(inventory.removeMapByCoords(player.level(), mapX, mapZ).map);
                AtlasesNetwork.UpdateInventory.send(player, atlasSlot);
            }
            case FROM_HAND -> {
                ItemStack heldItem = player.containerMenu.getCarried();
                if (heldItem.getItem() == Items.FILLED_MAP) {
                    Integer mapId = MapItem.getMapId(heldItem);
                    MapItemSavedData mapState = MapItem.getSavedData(mapId, player.level());
                    if (mapState != null && mapState.scale == inventory.getScale()) {
                        inventory.addToInventory(player.level(), heldItem);
                        player.containerMenu.setCarried(ItemStack.EMPTY);
                        AtlasesNetwork.UpdateInventory.send(player, atlasSlot);
                    }
                }
            }
            case FROM_INVENTORY -> {
                ItemStack stack = player.getInventory().getItem(mapX);
                if (stack.getItem() == Items.FILLED_MAP) {
                    Integer mapId = MapItem.getMapId(stack);
                    MapItemSavedData mapState = MapItem.getSavedData(mapId, player.level());
                    if (mapState != null && mapState.scale == inventory.getScale()) {
                        inventory.addToInventory(player.level(), stack);
                        player.getInventory().removeItemNoUpdate(mapX);
                        AtlasesNetwork.UpdateInventory.send(player, atlasSlot);
                    }
                }
            }
        }
    }

    private void handlePlayerLogin(Player player) {
        if (!player.level().isClientSide()) {
            AtlasesNetwork.SwappedAtlasSlot.send(player, -1);
        }
    }

    private void handlePlayerTick(Player player) {
        if (!player.level().isClientSide) {
            var serverPlayer = (ServerPlayer) player;

            for (var hand : InteractionHand.values()) {
                var held = serverPlayer.getItemInHand(hand);

                if (held.getItem() == ITEM.get()) {
                    var inventory = AtlasInventory.get(serverPlayer.level(), held);
                    if (inventory.updateActiveMap(serverPlayer)) {
                        var slot = getSlotFromHand(serverPlayer, hand);
                        AtlasesNetwork.UpdateInventory.send(player, slot);

                        if (inventory.getMapInfos().size() >= NUMBER_OF_MAPS_FOR_ACHIEVEMENT) {
                            AdvancementHelper.trigger(Charm.instance().makeId("made_atlas_maps"), serverPlayer);
                        }
                    }
                }
            }
        }
    }

    /**
     * Callback from {@link MapItemSavedDataMixin} to check
     * if player is holding a map or a player is holding an atlas that contains a map.
     * @param inventory Inventory to check.
     * @param stack Map.
     * @return True if the player has a map or the player has an atlas that has a map.
     */
    public static boolean doesAtlasContainMap(Inventory inventory, ItemStack stack) {
        if (inventory.contains(stack)) {
            return true;
        }

        for (var hand : InteractionHand.values()) {
            var atlasStack = inventory.player.getItemInHand(hand);
            if (atlasStack.getItem() == ITEM.get()) {
                var inv = AtlasInventory.get(inventory.player.level(), atlasStack);
                if (inv.hasItemStack(stack)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static void setupAtlasUpscale(Inventory playerInventory, CartographyTableMenu container) {
        var oldSlot = container.slots.get(0);
        container.slots.set(0, new Slot(oldSlot.container, oldSlot.index, oldSlot.x, oldSlot.y) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return oldSlot.mayPlace(stack) || stack.getItem() == ITEM.get()
                    && AtlasInventory.get(playerInventory.player.level(), stack).getMapInfos().isEmpty();
            }
        });
    }

    public static boolean makeAtlasUpscaleOutput(ItemStack topStack, ItemStack bottomStack, ItemStack outputStack, Level level,
                                                 ResultContainer craftResultInventory, CartographyTableMenu cartographyContainer) {
        if (topStack.getItem() == ITEM.get()) {
            ItemStack output;
            var inventory = AtlasInventory.get(level, topStack);

            if (inventory.getMapInfos().isEmpty() && bottomStack.getItem() == Items.MAP && inventory.getScale() < 4) {
                output = topStack.copy();
                ItemNbtHelper.setUuid(output, AtlasInventory.ID, UUID.randomUUID());
                ItemNbtHelper.setInt(output, AtlasInventory.SCALE, inventory.getScale() + 1);
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

    public static int getSlotFromHand(Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            return player.getInventory().selected;
        } else {
            return player.getInventory().getContainerSize() - 1;
        }
    }

    public static void handleSwappedSlot(AtlasesNetwork.SwapAtlasSlot request, Player player) {
        var swappedSlot = request.getSlot();
        var offhandItem = player.getOffhandItem().copy();
        var mainHandItem = player.getMainHandItem().copy();
        var inventory = player.getInventory();

        Consumer<Integer> doSwap = i -> {
            var swap = inventory.getItem(i).copy();
            inventory.setItem(i, mainHandItem);
            player.setItemInHand(InteractionHand.MAIN_HAND, swap);
            AtlasesNetwork.SwappedAtlasSlot.send(player, i);
        };

        if (mainHandItem.getItem() instanceof AtlasItem) {
            if (swappedSlot >= 0) {
                doSwap.accept(swappedSlot);
                return;
            }
        }

        if (offhandItem.getItem() instanceof AtlasItem) return;

        int slot = -1;
        for (int i = 0; i < 36; i++) {
            ItemStack inv = inventory.getItem(i);
            if (inv.getItem() instanceof AtlasItem) {
                slot = i;
                break;
            }
        }

        if (slot == -1) return;
        doSwap.accept(slot);
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return ITEM.get();
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public int getCost() {
                return 5;
            }
        });
    }
}
