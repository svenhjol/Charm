package svenhjol.charm.module.ender_bundles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.item.CharmItem;
import svenhjol.charm.loader.CharmModule;

import java.util.Optional;

public class EnderBundleItem extends CharmItem {
    public EnderBundleItem(CharmModule module) {
        super(module, "ender_bundle", (new Properties()
            .stacksTo(1)
            .tab(CreativeModeTab.TAB_TOOLS)));
    }
    
    @Environment(EnvType.CLIENT)
    public static float getAmountFilled() {
        return EnderBundlesClient.CACHED_AMOUNT_FILLED;
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public boolean isBarVisible(ItemStack stack) {
        return EnderBundlesClient.CACHED_AMOUNT_FILLED > 0;
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.min(13, (int)(12 * EnderBundlesClient.CACHED_AMOUNT_FILLED));
    }
    
    @Environment(EnvType.CLIENT)
    @Override
    public int getBarColor(ItemStack stack) {
        return EnderBundlesClient.ITEM_BAR_COLOR;
    }
    
    @Override
    public boolean overrideStackedOnOther(ItemStack bundle, Slot slot, ClickAction clickAction, Player player) {
        if (player.containerMenu instanceof ChestMenu
            && !(slot.container instanceof Inventory)) {
            return false; // don't allow inside ender chest inventory
        } else if (player.getAbilities().instabuild) {
            return false; // TODO: why is creative container wack?
        } else if (clickAction != ClickAction.SECONDARY) {
            return false;
        } else {
            var itemStack = slot.getItem();
            if (itemStack.isEmpty()) {
                var out = removeFirstStack(player);
                out.ifPresent(slot::safeInsert);
            } else if (itemStack.getItem().canFitInsideContainerItems()) {
                var out = addToBundle(player, itemStack);
                itemStack.setCount(out.getCount());
            }
            player.containerMenu.broadcastChanges();
        }
    
        return true;
    }
    
    @Override
    public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack otherStack, Slot slot, ClickAction clickAction, Player player, SlotAccess slotAccess) {
        if (player.containerMenu instanceof ChestMenu
            && !(slot.container instanceof Inventory)
        ) {
            return false; // don't allow inside ender chest inventory
        } else if (player.getAbilities().instabuild) {
            return false; // TODO: why is creative container wack?
        } else if (clickAction == ClickAction.SECONDARY && slot.allowModification(player)) {
            if (otherStack.isEmpty()) {
                removeFirstStack(player).ifPresent(slotAccess::set);
            } else {
                var out = addToBundle(player, otherStack);
                otherStack.setCount(out.getCount());
            }
            player.containerMenu.broadcastChanges();
            return true;
        }
        return false;
    }
    
    private static ItemStack addToBundle(Player player, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().canFitInsideContainerItems()) {
            var inventory = player.getEnderChestInventory();
            var out = inventory.addItem(stack);
            inventory.setChanged();
            
            if (!player.level.isClientSide) {
                EnderBundles.triggerUsedEnderBundle((ServerPlayer) player);
            }
            
            return out;
        }
        
        return stack;
    }
    
    private static Optional<ItemStack> removeFirstStack(Player player) {
        var inventory = player.getEnderChestInventory();
        var index = 0;
        var size = inventory.getContainerSize();
        for (int i = size - 1; i >= 0; i--) {
            if (!inventory.getItem(i).isEmpty()) {
                index = i;
            }
        }
        var stack = inventory.getItem(index);
        if (stack.isEmpty())
            return Optional.empty();
        
        var out = stack.copy();
        stack.setCount(0);
        inventory.setChanged();
        
        return Optional.of(out);
    }
}