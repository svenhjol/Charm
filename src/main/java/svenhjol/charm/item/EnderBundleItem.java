package svenhjol.charm.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CommandItemSlot;
import net.minecraft.inventory.EnderChestInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ClickType;
import net.minecraft.util.math.MathHelper;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.CharmItem;
import svenhjol.charm.client.EnderBundlesClient;

import java.util.Optional;

public class EnderBundleItem extends CharmItem {
    private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4F, 0.4F, 1.0F);

    public EnderBundleItem(CharmModule module) {
        super(module, "ender_bundle", (new Item.Settings())
            .maxCount(1)
            .group(ItemGroup.TOOLS));
    }

    @Environment(EnvType.CLIENT)
    public static float getAmountFilled() {
        return EnderBundlesClient.CACHED_AMOUNT_FILLED;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return EnderBundlesClient.CACHED_AMOUNT_FILLED > 0;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getItemBarStep(ItemStack stack) {
        return Math.min(13, (int)(12 * EnderBundlesClient.CACHED_AMOUNT_FILLED));
    }

    @Environment(EnvType.CLIENT)
    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    @Override
    public boolean onStackClicked(ItemStack bundle, Slot slot, ClickType clickType, PlayerEntity player) {
        if (player.currentScreenHandler instanceof GenericContainerScreenHandler
            && !(slot.inventory instanceof PlayerInventory)) {
            return false; // don't allow inside ender chest inventory
        } else if (player.currentScreenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            return false; // TODO: why is creative container wack?
        } else if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            ItemStack itemStack = slot.getStack();
            if (itemStack.isEmpty()) {
                Optional<ItemStack> out = removeLastStack(player);
                out.ifPresent(slot::method_32756);
            } else if (itemStack.getItem().hasStoredInventory()) {
                ItemStack out = addToBundle(player, itemStack);
                itemStack.setCount(out.getCount());
            }
            player.currentScreenHandler.sendContentUpdates();
        }

        return true;
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, CommandItemSlot commandItemSlot) {
        if (player.currentScreenHandler instanceof GenericContainerScreenHandler
            && !(slot.inventory instanceof PlayerInventory)
        ) {
            return false; // don't allow inside ender chest inventory
        } else if (player.currentScreenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            return false; // TODO: why is creative contains wack?
        } else if (clickType == ClickType.RIGHT && slot.method_32754(player)) {
            if (otherStack.isEmpty()) {
                removeLastStack(player).ifPresent(commandItemSlot::set);
            } else {
                ItemStack out = addToBundle(player, otherStack);
                otherStack.setCount(out.getCount());
            }
            player.currentScreenHandler.sendContentUpdates();
            return true;
        }
        return false;
    }

    private static ItemStack addToBundle(PlayerEntity player, ItemStack stack) {
        if (!stack.isEmpty() && stack.getItem().hasStoredInventory()) {
            EnderChestInventory inventory = player.getEnderChestInventory();
            ItemStack out = inventory.addStack(stack);
            inventory.markDirty();
            return out;
        }

        return stack;
    }

    private static Optional<ItemStack> removeLastStack(PlayerEntity player) {
        EnderChestInventory inventory = player.getEnderChestInventory();
        int index = 0;
        int size = inventory.size();
        for (int i = 0; i < size; i++) {
            if (!inventory.getStack(i).isEmpty()) {
                index = i;
            }
        }
        ItemStack stack = inventory.getStack(index);
        if (stack.isEmpty())
            return Optional.empty();

        ItemStack out = stack.copy();
        stack.setCount(0);
        inventory.markDirty();

        return Optional.of(out);
    }
}
