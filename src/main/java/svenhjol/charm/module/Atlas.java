package svenhjol.charm.module;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.NetworkSyncedItem;
import net.minecraft.network.Packet;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.handler.RegistryHandler;
import svenhjol.charm.screenhandler.AtlasInventory;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.AtlasClient;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.item.AtlasItem;
import svenhjol.charm.screenhandler.AtlasContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

@Module(mod = Charm.MOD_ID, client = AtlasClient.class, description = "Storage for maps that automatically updates the displayed map as you explore.")
public class Atlas extends CharmModule {
    public static final Identifier ID = new Identifier(Charm.MOD_ID, "atlas");
    // add items to this list to whitelist them in atlases
    public static final List<Item> VALID_ATLAS_ITEMS = new ArrayList<>();
    private static final WeakHashMap<ItemStack, AtlasInventory> cache = new WeakHashMap<>();

    @Config(name = "Map Size", description = "The atlas will create maps of this size (0-4).")
    public static int mapSize = 2;

    @Config(name = "Open in off hand", description = "Allow opening the atlas while it is in the off hand.")
    public static boolean offHandOpen = false;

    public static AtlasItem ATLAS_ITEM;
    public static ScreenHandlerType<AtlasContainer> CONTAINER;

    public static boolean canAtlasInsertItem(ItemStack stack) {
        return VALID_ATLAS_ITEMS.contains(stack.getItem());
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
        AtlasInventory inventory = cache.get(stack);
        if (inventory == null) {
            inventory = new AtlasInventory(world, stack, stack.getName());
            cache.put(stack, inventory);
        }
        return inventory;
    }

    public static void sendMapToClient(ServerPlayerEntity player, ItemStack map, int slot) {
        if (map.getItem().isNetworkSynced()) {
            map.getItem().inventoryTick(map, player.world, player, slot, true);
            Packet<?> packet = ((NetworkSyncedItem) map.getItem()).createSyncPacket(map, player.world, player);
            if (packet != null) {
                player.networkHandler.sendPacket(packet);
            }
        }
    }


    @Override
    public void register() {
        ATLAS_ITEM = new AtlasItem(this);

        VALID_ATLAS_ITEMS.add(Items.MAP);
        VALID_ATLAS_ITEMS.add(Items.FILLED_MAP);

        CONTAINER = RegistryHandler.screenHandler(ID, AtlasContainer::new);
    }

    @Override
    public void init() {
        PlayerTickCallback.EVENT.register(this::handlePlayerTick);
    }

    private void handlePlayerTick(PlayerEntity player) {
        if (player.world.isClient)
            return;

        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        for (Hand hand : Hand.values()) {
            ItemStack atlasStack = serverPlayer.getStackInHand(hand);
            if (atlasStack.getItem() == ATLAS_ITEM) {
                AtlasInventory inventory = getInventory(serverPlayer.world, atlasStack);
                AtlasInventory.MapInfo mapInfo = inventory.updateActiveMap(serverPlayer);
                if (mapInfo != null) {
                    ItemStack map = inventory.getStack(mapInfo.slot);
                    sendMapToClient(serverPlayer, map, mapInfo.slot);
                }
            }
        }
    }
}
