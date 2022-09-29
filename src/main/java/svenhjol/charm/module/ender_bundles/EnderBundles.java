package svenhjol.charm.module.ender_bundles;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.api.event.HoverSortItemsCallback;
import svenhjol.charm.helper.TextHelper;
import svenhjol.charm.lib.Advancements;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.inventory_tidying.InventoryTidyingHandler;
import svenhjol.charm.module.ender_bundles.network.ServerReceiveOpenEnderBundle;
import svenhjol.charm.module.ender_bundles.network.ServerReceiveRequestEnderInventory;
import svenhjol.charm.module.ender_bundles.network.ServerSendUpdateEnderInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@CommonModule(mod = Charm.MOD_ID)
public class EnderBundles extends CharmModule {
    private static final List<Predicate<Player>> INVENTORY_CHECKS = new ArrayList<>();
    private static final Component CONTAINER_TITLE = TextHelper.translatable("container.charm.ender_bundle");
    private static final ServerReceiveOpenEnderBundle RECEIVE_OPEN_ENDER_CHEST = new ServerReceiveOpenEnderBundle();
    private static final ServerReceiveRequestEnderInventory RECEIVE_REQUEST_ENDER_INVENTORY = new ServerReceiveRequestEnderInventory();
    private static final ServerSendUpdateEnderInventory SEND_UPDATE_ENDER_INVENTORY = new ServerSendUpdateEnderInventory();
    private static final ResourceLocation TRIGGER_USED_ENDER_BUNDLE = new ResourceLocation(Charm.MOD_ID, "used_ender_bundle");
    
    public static EnderBundleItem ENDER_BUNDLE;
    public static final String ENDER_ITEMS_TAG = "EnderItems";
    
    @Config(name = "Enable keybind", description = "If true, sets a keybind for opening the player's Ender Bundle inventory (defaults to 'b').")
    public static boolean enableKeybind = true;
    
    @Override
    public void register() {
        ENDER_BUNDLE = new EnderBundleItem(this);
    }
    
    @Override
    public void runWhenEnabled() {
        HoverSortItemsCallback.EVENT.register(this::handleSortItems);
        
        registerInventoryCheck(player -> {
            List<ItemStack> items = new ArrayList<>();
            items.addAll(player.inventory.items);
            items.addAll(player.inventory.offhand);
            return items.stream().anyMatch(stack -> stack.is(ENDER_BUNDLE));
        });
    }
    
    public static void registerInventoryCheck(Predicate<Player> predicate) {
        INVENTORY_CHECKS.add(predicate);
    }
    
    public static boolean hasEnderBundle(Player player) {
        return INVENTORY_CHECKS.stream().anyMatch(check -> check.test(player));
    }
    
    public static void handleRequestEnderInventory(ServerPlayer player) {
        SEND_UPDATE_ENDER_INVENTORY.send(player);
    }
    
    public static void handleReceiveOpenEnderBundle(ServerPlayer player) {
        if (!hasEnderBundle(player)) return;
    
        var enderChestInventory = player.getEnderChestInventory();
        player.closeContainer();
        player.openMenu(new SimpleMenuProvider((i, inv, p) 
            -> ChestMenu.threeRows(i, inv, enderChestInventory), CONTAINER_TITLE));
    }
    
    public static void triggerUsedEnderBundle(ServerPlayer player) {
        Advancements.triggerActionPerformed(player, EnderBundles.TRIGGER_USED_ENDER_BUNDLE);
    }
    
    public static NonNullList<ItemStack> getEnderInventory(Player player) {
        var inventory = player.getEnderChestInventory();
        var size = inventory.getContainerSize();
        var items = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
        
        for (int i = 0; i < size; i++) {
            items.set(i, inventory.getItem(i));
        }
        return items;
    }
    
    private void handleSortItems(ServerPlayer player, ItemStack itemStack, boolean direction) {
        if (itemStack.is(ENDER_BUNDLE)) {
            var items = new ArrayList<>(getEnderInventory(player));
            InventoryTidyingHandler.mergeStacks(items);
            HoverSortItemsCallback.sortByScrollDirection(items, direction);
    
            var inventory = player.getEnderChestInventory();
            var size = inventory.getContainerSize();
    
            for (int i = 0; i < size; i++) {
                if (i < items.size()) {
                    inventory.setItem(i, items.get(i));
                } else {
                    inventory.setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }
}