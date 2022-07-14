package svenhjol.charm.module.atlases;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CartographyTableMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.PlayerTickCallback;
import svenhjol.charm.helper.ItemNbtHelper;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.atlases.network.ServerReceiveSwapAtlas;
import svenhjol.charm.module.atlases.network.ServerReceiveTransferAtlas;
import svenhjol.charm.module.atlases.network.ServerSendSwappedSlot;
import svenhjol.charm.module.atlases.network.ServerSendUpdateInventory;
import svenhjol.charm.registry.CommonRegistry;

import java.util.*;

@CommonModule(mod = Charm.MOD_ID, description = "Storage for maps that automatically updates the displayed map as you explore.")
public class Atlases extends CharmModule {
    public static final ResourceLocation ID = new ResourceLocation(Charm.MOD_ID, "atlas");
    public static final ResourceLocation TRIGGER_MADE_ATLAS_MAPS = new ResourceLocation(Charm.MOD_ID, "made_atlas_maps");

    public static ServerSendSwappedSlot SERVER_SEND_SWAPPED_SLOT;
    public static ServerSendUpdateInventory SERVER_SEND_UPDATE_INVENTORY;
    public static ServerReceiveSwapAtlas SERVER_RECEIVE_SWAP_ATLAS;
    public static ServerReceiveTransferAtlas SERVER_RECEIVE_TRANSFER_ATLAS;

    public static SoundEvent ATLAS_OPEN_SOUND;
    public static SoundEvent ATLAS_CLOSE_SOUND;

    public static final int NUMBER_OF_MAPS_FOR_ACHIEVEMENT = 10;

    // add items to this list to whitelist them in atlases
    public static final List<Item> VALID_ATLAS_ITEMS = new ArrayList<>();
    private static final Map<UUID, AtlasInventory> serverCache = new HashMap<>();
    private static final Map<UUID, AtlasInventory> clientCache = new HashMap<>();

    @Config(name = "Open in off hand", description = "Allow opening the atlas while it is in the off hand.")
    public static boolean offHandOpen = false;

    @Config(name = "Map scale", description = "Map scale used in atlases by default.")
    public static int defaultScale = 0;

    @Config(name = "Enable keybind", description = "If true, sets a keybind for swapping the item in your main hand with the first available atlas in your inventory (defaults to 'r').")
    public static boolean enableKeybind = true;

    public static AtlasItem ATLAS_ITEM;
    public static MenuType<AtlasContainer> MENU;

    @Override
    public void register() {
        ATLAS_ITEM = new AtlasItem(this);
        VALID_ATLAS_ITEMS.add(Items.MAP);
        VALID_ATLAS_ITEMS.add(Items.FILLED_MAP);
        ATLAS_OPEN_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "atlas_open"));
        ATLAS_CLOSE_SOUND = CommonRegistry.sound(new ResourceLocation(Charm.MOD_ID, "atlas_close"));
        MENU = CommonRegistry.menu(ID, (syncId, playerInventory) -> new AtlasContainer(syncId, playerInventory, findAtlas(playerInventory)));
    }

    @Override
    public void runWhenEnabled() {
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
        ServerPlayConnectionEvents.JOIN.register(this::handlePlayerJoin);

        SERVER_SEND_SWAPPED_SLOT = new ServerSendSwappedSlot();
        SERVER_SEND_UPDATE_INVENTORY = new ServerSendUpdateInventory();
        SERVER_RECEIVE_SWAP_ATLAS = new ServerReceiveSwapAtlas();
        SERVER_RECEIVE_TRANSFER_ATLAS = new ServerReceiveTransferAtlas();
    }

    public static boolean inventoryContainsMap(Inventory inventory, ItemStack stack) {
        if (inventory.contains(stack)) {
            return true;
        } else if (Charm.LOADER.isEnabled(Atlases.class)) {
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack atlasStack = inventory.player.getItemInHand(hand);
                if (atlasStack.getItem() == ATLAS_ITEM) {
                    AtlasInventory inv = getInventory(inventory.player.level, atlasStack);
                    if (inv.hasItemStack(stack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static AtlasInventory getInventory(Level level, ItemStack stack) {
        UUID id = ItemNbtHelper.getUuid(stack, AtlasInventory.ID);
        if (id == null) {
            id = UUID.randomUUID();
            ItemNbtHelper.setUuid(stack, AtlasInventory.ID, id);
        }
        Map<UUID, AtlasInventory> cache = level.isClientSide ? clientCache : serverCache;
        AtlasInventory inventory = cache.get(id);
        if (inventory == null) {
            inventory = new AtlasInventory(stack);
            cache.put(id, inventory);
        }
        if(inventory.getAtlasItem() != stack) {
            inventory.reload(stack);
        }
        return inventory;
    }

    public static void sendMapToClient(ServerPlayer player, ItemStack map, boolean markDirty) {
        if (map.getItem().isComplex()) {
            if(markDirty) {
                Integer mapId = MapItem.getMapId(map);
                MapItemSavedData mapState = MapItem.getSavedData(mapId, player.level);

                if (mapState == null) {
                    return;
                }

                mapState.setColorsDirty(0, 0);
            }
            map.getItem().inventoryTick(map, player.level, player, -1, true);
            Packet<?> packet = ((ComplexItem) map.getItem()).getUpdatePacket(map, player.level, player);
            if (packet != null) {
                player.connection.send(packet);
            }
        }
    }

    public static void updateClient(ServerPlayer player, int atlasSlot) {
        SERVER_SEND_UPDATE_INVENTORY.send(player, atlasSlot);
    }

    private static AtlasInventory findAtlas(Inventory inventory) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = inventory.player.getItemInHand(hand);
            if (stack.getItem() == ATLAS_ITEM) {
                return getInventory(inventory.player.level, stack);
            }
        }
        throw new IllegalStateException("No atlas in any hand, can't open!");
    }

    public static void setupAtlasUpscale(Inventory playerInventory, CartographyTableMenu container) {
        if (Charm.LOADER.isEnabled(Atlases.class)) {
            Slot oldSlot = container.slots.get(0);
            container.slots.set(0, new Slot(oldSlot.container, oldSlot.index, oldSlot.x, oldSlot.y) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return oldSlot.mayPlace(stack) || stack.getItem() == ATLAS_ITEM && getInventory(playerInventory.player.level, stack).getMapInfos().isEmpty();
                }
            });
        }
    }

    public static boolean makeAtlasUpscaleOutput(ItemStack topStack, ItemStack bottomStack, ItemStack outputStack, Level level,
        ResultContainer craftResultInventory, CartographyTableMenu cartographyContainer) {
        if (Charm.LOADER.isEnabled(Atlases.class) && topStack.getItem() == ATLAS_ITEM) {
            AtlasInventory inventory = getInventory(level, topStack);
            ItemStack output;
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

    public static void triggerMadeMaps(ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, TRIGGER_MADE_ATLAS_MAPS);
    }

    private void handlePlayerTick(Player player) {
        if (!player.level.isClientSide) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            for (InteractionHand hand : InteractionHand.values()) {
                ItemStack atlas = serverPlayer.getItemInHand(hand);
                if (atlas.getItem() == ATLAS_ITEM) {
                    AtlasInventory inventory = getInventory(serverPlayer.level, atlas);
                    if (inventory.updateActiveMap(serverPlayer)) {
                        updateClient(serverPlayer, getSlotFromHand(serverPlayer, hand));
                        if (inventory.getMapInfos().size() >= NUMBER_OF_MAPS_FOR_ACHIEVEMENT) {
                            triggerMadeMaps((ServerPlayer) player);
                        }
                    }
                }
            }
        }
    }

    private void handlePlayerJoin(ServerGamePacketListenerImpl listener, PacketSender sender, MinecraftServer server) {
        // We need to reset the swapped slot when a player joins.
        // This is because a player connecting from a different world
        // could retain their swapped slot status when joining this one.
        SERVER_SEND_SWAPPED_SLOT.send(listener.getPlayer(), -1);
    }

    private static int getSlotFromHand(Player player, InteractionHand hand) {
        if(hand == InteractionHand.MAIN_HAND) {
            return player.getInventory().selected;
        } else {
            return player.getInventory().getContainerSize() - 1;
        }
    }

    public enum MoveMode {
        TO_HAND, TO_INVENTORY, FROM_HAND, FROM_INVENTORY
    }
}
