package svenhjol.charm.charmony.helper;

import net.minecraft.world.item.ItemStack;

public final class TooltipHelper {
    public static final ThreadLocal<ItemStack> tooltipItemStackHolder = new ThreadLocal<>();

    public static ItemStack getTooltipItemStack() {
        var current = tooltipItemStackHolder.get();

        if (current == null) {
            return ItemStack.EMPTY;
        }

        return current;
    }

    public static void setTooltipItemStack(ItemStack stack) {
        tooltipItemStackHolder.set(stack);
    }

    public static void clearTooltipItemStack() {
        tooltipItemStackHolder.set(ItemStack.EMPTY);
    }
}
