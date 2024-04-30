package svenhjol.charm.feature.atlases;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charm.api.CharmApi;
import svenhjol.charm.api.iface.IWandererTrade;
import svenhjol.charm.api.iface.IWandererTradeProvider;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.feature.atlases.CommonNetworking.C2SSwapAtlasSlot;
import svenhjol.charm.feature.atlases.CommonNetworking.C2STransferAtlas;
import svenhjol.charm.feature.atlases.CommonNetworking.S2CSwappedAtlasSlot;
import svenhjol.charm.feature.atlases.CommonNetworking.S2CUpdateInventory;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Networking;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Atlases extends CommonFeature implements IWandererTradeProvider {
    public static final int EMPTY_MAP_SLOTS = 3;
    public static final int NUMBER_OF_MAPS_FOR_ACHIEVEMENT = 10;

    static Supplier<DataComponentType<AtlasData>> atlasData;
    static Supplier<DataComponentType<MapData>> mapData;
    static Supplier<Item> item;
    static Supplier<MenuType<AtlasContainer>> menuType;
    static Supplier<SoundEvent> openSound;
    static Supplier<SoundEvent> closeSound;

    @Configurable(name = "Open in off hand", description = "Allow opening the atlas while it is in the off-hand.")
    public static boolean offHandOpen = false;

    @Configurable(name = "Map scale", description = "Map scale used in atlases by default.")
    public static int defaultScale = 0;

    @Override
    public String description() {
        return "Storage for maps that automatically updates the displayed map as you explore.";
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        CharmApi.registerProvider(this);
        return Optional.of(new CommonRegistration(this));
    }

    @Override
    public Optional<Networking<? extends Feature>> networking() {
        return Optional.of(new CommonNetworking(this));
    }

    public static void handleSwappedSlot(Player player, C2SSwapAtlasSlot request) {
        var swappedSlot = request.getSlot();
        var offhandItem = player.getOffhandItem().copy();
        var mainHandItem = player.getMainHandItem().copy();
        var inventory = player.getInventory();

        Consumer<Integer> doSwap = i -> {
            var swap = inventory.getItem(i).copy();
            inventory.setItem(i, mainHandItem);
            player.setItemInHand(InteractionHand.MAIN_HAND, swap);
            S2CSwappedAtlasSlot.send((ServerPlayer)player, i);
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

    public static void handleTransferAtlas(Player player, C2STransferAtlas request) {
        var serverPlayer = (ServerPlayer)player;
        var atlasSlot = request.getAtlasSlot();
        var mapX = request.getMapX();
        var mapZ = request.getMapZ();
        var inventory = AtlasInventory.get(serverPlayer.level(), serverPlayer.getInventory().getItem(atlasSlot));

        switch (request.getMoveMode()) {
            case TO_HAND -> {
                serverPlayer.containerMenu.setCarried(inventory.removeMapByCoords(serverPlayer.level(), mapX, mapZ).map);
                S2CUpdateInventory.send(serverPlayer, atlasSlot);
            }
            case TO_INVENTORY -> {
                serverPlayer.addItem(inventory.removeMapByCoords(serverPlayer.level(), mapX, mapZ).map);
                S2CUpdateInventory.send(serverPlayer, atlasSlot);
            }
            case FROM_HAND -> {
                ItemStack stack = serverPlayer.containerMenu.getCarried();
                if (stack.getItem() == Items.FILLED_MAP) {
                    var mapState = MapItem.getSavedData(stack, serverPlayer.level());
                    if (mapState != null && mapState.scale == inventory.getScale()) {
                        inventory.addToInventory(serverPlayer.level(), stack);
                        serverPlayer.containerMenu.setCarried(ItemStack.EMPTY);
                        S2CUpdateInventory.send(serverPlayer, atlasSlot);
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
                        S2CUpdateInventory.send(serverPlayer, atlasSlot);
                    }
                }
            }
        }
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of(new IWandererTrade() {
            @Override
            public ItemLike getItem() {
                return item.get();
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

    public static void triggerMadeAtlasMaps(Player player) {
        Advancements.trigger(Charm.id("made_atlas_maps"), player);
    }
}
