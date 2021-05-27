package svenhjol.charm.mixin.core;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.helper.ItemHelper;

@Mixin(ItemEntity.class)
public class CheckItemDespawnMixin {

    /**
     * Defer to shouldItemDespawn.
     * If the check is false (the item should NOT despawn), then
     * return early from this vanilla method.
     */
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/ItemEntity;discard()V",
            ordinal = 1
        ),
        cancellable = true
    )
    private void hookTick(CallbackInfo ci) {
        if (!ItemHelper.shouldItemDespawn((ItemEntity)(Object)this))
            ci.cancel();
    }
}
