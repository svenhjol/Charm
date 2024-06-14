package svenhjol.charm.mixin.feature.piglin_pointing;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.piglin_pointing.PiglinPointing;

@Mixin(PiglinAi.class)
public abstract class PiglinAiMixin {

    @ModifyReturnValue(
            method = "isLovedItem",
            at = @At("RETURN")
    )
    private static boolean hookIsLovedItem(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (Resolve.feature(PiglinPointing.class).handlers.isBarteringItem(stack)) {
            return true;
        }
        return original;
    }

    @Inject(
        method = "wantsToPickup",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookWantsToPickup(Piglin piglin, ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (Resolve.feature(PiglinPointing.class).handlers.wantsToPickup(piglin, stack)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(
        method = "pickUpItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isLovedItem(Lnet/minecraft/world/item/ItemStack;)Z",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private static void hookCheckBeforeLovedItemCheck(Piglin piglin, ItemEntity itemEntity, CallbackInfo ci, @Local ItemStack stack) {
        if (Resolve.feature(PiglinPointing.class).handlers.tryToPickup(piglin, stack)) {
            ci.cancel();
        }
    }

    @Inject(
        method = "stopHoldingOffHandItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/monster/piglin/PiglinAi;isBarterCurrency(Lnet/minecraft/world/item/ItemStack;)Z",
            shift = At.Shift.BEFORE
        )
    )
    private static void hookCheckBeforeBarterCurrency(Piglin piglin, boolean bl, CallbackInfo ci, @Local ItemStack stack) {
        Resolve.feature(PiglinPointing.class).handlers.checkBlockAndFindStructure(piglin, stack);
    }

    @Inject(
        method = "updateActivity",
        at = @At("HEAD")
    )
    private static void hookUpdatePointing(Piglin piglin, CallbackInfo ci) {
        Resolve.feature(PiglinPointing.class).handlers.setPointing(piglin);
    }
}
