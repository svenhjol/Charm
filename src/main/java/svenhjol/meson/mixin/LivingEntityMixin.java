package svenhjol.meson.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.meson.event.EntityDeathCallback;
import svenhjol.meson.event.EntityDropsCallback;
import svenhjol.meson.event.HurtEntityCallback;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "drop", at = @At("TAIL"))
    private void hookDrop(DamageSource source, CallbackInfo ci) {
        LivingEntity entity = (LivingEntity)(Object)this;
        int lootingLevel = EnchantmentHelper.getLooting(entity);

        EntityDropsCallback.EVENT.invoker().interact(entity, source, lootingLevel);
    }

    @Inject(
        method = "applyDamage",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookApplyDamage(DamageSource source, float amount, CallbackInfo ci) {
        ActionResult result = HurtEntityCallback.EVENT.invoker().interact((LivingEntity) (Object) this, source, amount);
        if (result == ActionResult.FAIL)
            ci.cancel();
    }

    @Inject(
        method = "onDeath",
        at = @At("HEAD")
    )
    private void hookOnDeath(DamageSource source, CallbackInfo ci) {
        EntityDeathCallback.EVENT.invoker().interact((LivingEntity)(Object)this, source);
    }
}
