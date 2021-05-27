package svenhjol.charm.mixin.stackable_stews;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SuspiciousStewItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.module.stackable_stews.StackableStews;

@Mixin(SuspiciousStewItem.class)
public class FinishEatingSuspiciousStewMixin {

    /**
     * Defer to tryEatStewStack when suspicious stew is eaten.
     * If the check passes, return the decremented stack.
     *
     * If this mixin is not present, vanilla will reduce an
     * entire stack of stew to a single bowl.
     */
    @Inject(
        method = "finishUsing",
        at = @At(value = "TAIL"),
        cancellable = true,
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookFinishUsing(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir, ItemStack eatenStack) {
        boolean result = StackableStews.tryEatStewStack(entity, eatenStack);
        if (result)
            cir.setReturnValue(eatenStack);
    }
}
