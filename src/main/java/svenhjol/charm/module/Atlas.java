package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.PacketContext;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.*;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.CartographyTableScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.base.helper.ItemNBTHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.AtlasClient;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.item.AtlasItem;
import svenhjol.charm.mixin.accessor.SlotAccessor;
import svenhjol.charm.screenhandler.AtlasContainer;
import svenhjol.charm.screenhandler.AtlasInventory;

import java.util.*;

@Module(mod = Charm.MOD_ID, client = AtlasClient.class, description = "Storage for maps that automatically updates the displayed map as you explore.")
public class Atlas extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "atlas");
    public static final Identifier MSG_SERVER_ATLAS_TRANSFER = new Identifier(Charm.MOD_ID, "server_atlas_transfer");
    public static final Identifier MSG_CLIENT_UPDATE_ATLAS_INVENTORY = new Identifier(Charm.MOD_ID, "client_update_atlas_inventory");

    // add items to this list to whitelist them in atlases
    public static final List<Item> VALID_ATLAS_ITEMS = new ArrayList<>();
    private static final Map<UUID, AtlasInventory> serverCache = new HashMap<>();
    private static final Map<UUID, AtlasInventory> clientCache = new HashMap<>();

    @Config(name = "Open in off hand", description = "Allow opening the atlas while it is in the off hand.")
    public static boolean offHandOpen = false;

    @Config(name = "Map scale", description = "Map scale used in atlases by default.")
    public static int defaultScale = 0;

    public static AtlasItem ATLAS_ITEM;
    public static ScreenHandlerType<AtlasContainer> CONTAINER;

    @Override
    public void register() {
        ATLAS_ITEM = new AtlasItem(this);

        VALID_ATLAS_ITEMS.add(Items.MAP);
        VALID_ATLAS_ITEMS.add(Items.FILLED_MAP);

        CONTAINER = RegistryHandler.screenHandler(ID, (syncId, playerInventory) -> new AtlasContainer(syncId, playerInventory, findAtlas(playerInventory)));
    }

    @Override
    public void init() {
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);

        // listen for network requests to run the server callback
        ServerSidePacketRegistry.INSTANCE.register(MSG_SERVER_ATLAS_TRANSFER, this::handleServerAtlasTransfer);
    }

    public static boolean inventoryContainsMap(PlayerInventory inventory, ItemStack itemStack) {
        if (inventory.contains(itemStack)) {
            return true;
        } else if (ModuleHandler.enabled(Atlas.class)) {
            for (Hand hand : Hand.values()) {
                ItemStack atlasStack = inventory.player.getStackInHand(hand);
                if (atlasStack.getItem() == ATLAS_ITEM) {
                    AtlasInventory inv = getInventory(inventory.player.world, atlasStack);
                    if (inv.hasItemStack(itemStack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static AtlasInventory getInventory(World world, ItemStack stack) {
        UUID id = ItemNBTHelper.getUuid(stack, AtlasInventory.ID);
        if (id == null) {
            id = UUID.randomUUID();
            ItemNBTHelper.setUuid(stack, AtlasInventory.ID, id);
        }
        Map<UUID, AtlasInventory> cache = world.isClient ? clientCache : serverCache;
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

    public static void sendMapToClient(ServerPlayerEntity player, ItemStack map, boolean markDirty) {
        if (map.getItem().isNetworkSynced()) {
            if(markDirty) {
                FilledMapItem.getMapState(map, player.world).markDirty(0, 0);
            }
            map.getItem().inventoryTick(map, player.world, player, -1, true);
            Packet<?> packet = ((NetworkSyncedItem) map.getItem()).createSyncPacket(map, player.world, player);
            if (packet != null) {
                player.networkHandler.sendPacket(packet);
            }
        }
    }

    public static void updateClient(ServerPlayerEntity player, int atlasSlot) {
        PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
        data.writeInt(atlasSlot);
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, MSG_CLIENT_UPDATE_ATLAS_INVENTORY, data);
    }

    private static AtlasInventory findAtlas(PlayerInventory inventory) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = inventory.player.getStackInHand(hand);
            if (stack.getItem() == Atlas.ATLAS_ITEM) {
                return getInventory(inventory.player.world, stack);
            }
        }
        throw new IllegalStateException("No atlas in any hand, can't open!");
    }

    public static void setupAtlasUpscale(PlayerInventory playerInventory, CartographyTableScreenHandler container) {
        if (ModuleHandler.enabled(Atlas.class)) {
            Slot oldSlot = container.slots.get(0);
            container.slots.set(0, new Slot(oldSlot.inventory, ((SlotAccessor)oldSlot).getIndex(), oldSlot.x, oldSlot.y) {
                @Override
                public boolean canInsert(ItemStack stack) {
                    return oldSlot.canInsert(stack) || stack.getItem() == ATLAS_ITEM && getInventory(playerInventory.player.world, stack).getMapInfos().isEmpty();
                }
            });
        }
    }

    public static boolean makeAtlasUpscaleOutput(ItemStack topStack, ItemStack bottomStack, ItemStack outputStack, World world,
        CraftingResultInventory craftResultInventory, CartographyTableScreenHandler cartographyContainer) {
        if (ModuleHandler.enabled(Atlas.class) && topStack.getItem() == Atlas.ATLAS_ITEM) {
            AtlasInventory inventory = Atlas.getInventory(world, topStack);
            ItemStack output;
            if (inventory.getMapInfos().isEmpty() && bottomStack.getItem() == Items.MAP && inventory.getScale() < 4) {
                output = topStack.copy();
                ItemNBTHelper.setUuid(output, AtlasInventory.ID, UUID.randomUUID());
                ItemNBTHelper.setInt(output, AtlasInventory.SCALE, inventory.getScale() + 1);
            } else {
                output = ItemStack.EMPTY;
            }
            if (!ItemStack.areEqual(output, outputStack)) {
                craftResultInventory.setStack(2, output);
                cartographyContainer.sendContentUpdates();
            }
            return true;
        }
        return false;
    }

    private void handlePlayerTick(PlayerEntity player) {
        if (!player.world.isClient) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            for (Hand hand : Hand.values()) {
                ItemStack atlas = serverPlayer.getStackInHand(hand);
                if (atlas.getItem() == ATLAS_ITEM) {
                    AtlasInventory inventory = getInventory(serverPlayer.world, atlas);
                    if (inventory.updateActiveMap(serverPlayer)) {
                        updateClient(serverPlayer, getSlotFromHand(serverPlayer, hand));
                    }
                }
            }
        }
    }

    private void handleServerAtlasTransfer(PacketContext context, PacketByteBuf data) {
        int atlasSlot = data.readInt();
        int mapX = data.readInt();
        int mapZ = data.readInt();
        MoveMode mode = data.readEnumConstant(MoveMode.class);

        context.getTaskQueue().execute(() -> {
            ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
            if (player == null)
                return;

            AtlasInventory inventory = Atlas.getInventory(player.world, player.inventory.getStack(atlasSlot));
            switch (mode) {
                case TO_HAND:
                    player.inventory.setCursorStack(inventory.removeMapByCoords(player.world, mapX, mapZ).map);
                    player.updateCursorStack();
                    updateClient(player, atlasSlot);
                    break;
                case TO_INVENTORY:
                    player.giveItemStack(inventory.removeMapByCoords(player.world, mapX, mapZ).map);
                    updateClient(player, atlasSlot);
                    break;
                case FROM_HAND:
                    ItemStack heldItem = player.inventory.getCursorStack();
                    if (heldItem.getItem() == Items.FILLED_MAP && FilledMapItem.getMapState(heldItem, player.world).scale == inventory.getScale()) {
                        inventory.addToInventory(player.world, heldItem);
                        player.inventory.setCursorStack(ItemStack.EMPTY);
                        player.updateCursorStack();
                        updateClient(player, atlasSlot);
                    }
                    break;
                case FROM_INVENTORY:
                    ItemStack stack = player.inventory.getStack(mapX);
                    if (stack.getItem() == Items.FILLED_MAP && FilledMapItem.getMapState(stack, player.world).scale == inventory.getScale()) {
                        inventory.addToInventory(player.world, stack);
                        player.inventory.removeStack(mapX);
                        updateClient(player, atlasSlot);
                    }
                    break;
            }
        });
    }

    private static int getSlotFromHand(PlayerEntity player, Hand hand) {
        if(hand == Hand.MAIN_HAND) {
            return player.inventory.selectedSlot;
        } else {
            return player.inventory.size() - 1;
        }
    }

    public enum MoveMode {
        TO_HAND, TO_INVENTORY, FROM_HAND, FROM_INVENTORY
    }
}
