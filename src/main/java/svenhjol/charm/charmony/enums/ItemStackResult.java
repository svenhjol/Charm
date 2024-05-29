package svenhjol.charm.charmony.enums;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.item.ItemStack;

public record ItemStackResult(EventResult result, ItemStack stack, boolean isClient) {
    public InteractionResultHolder<ItemStack> asInteractionResultHolder() {
        return switch (result) {
            case CONSUME -> InteractionResultHolder.consume(stack);
            case CANCEL -> InteractionResultHolder.fail(stack);
            case SUCCESS -> InteractionResultHolder.sidedSuccess(stack, isClient());
            case NONE, PASS -> InteractionResultHolder.pass(stack);
        };
    }

    public static ItemStackResult success(ItemStack stack) {
        return success(stack, false);
    }

    public static ItemStackResult success(ItemStack stack, boolean isClient) {
        return new ItemStackResult(EventResult.SUCCESS, stack, isClient);
    }

    public static ItemStackResult cancel(ItemStack stack) {
        return new ItemStackResult(EventResult.CANCEL, stack, false);
    }

    public static ItemStackResult consume(ItemStack stack) {
        return new ItemStackResult(EventResult.CONSUME, stack, false);
    }

    public static ItemStackResult pass(ItemStack stack) {
        return new ItemStackResult(EventResult.PASS, stack, false);
    }

    public static ItemStackResult none() {
        return new ItemStackResult(EventResult.NONE, ItemStack.EMPTY, false);
    }
}
