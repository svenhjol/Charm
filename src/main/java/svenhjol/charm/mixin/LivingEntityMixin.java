package svenhjol.charm.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.EntityDropsCallback;
import svenhjol.charm.event.HurtEntityCallback;
import svenhjol.charm.module.ArmorInvisibility;
import svenhjol.charm.module.UseTotemFromInventory;
import svenhjol.meson.Meson;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract Iterable<ItemStack> getArmorItems();

    @Redirect(
        method = "tryUseTotem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack hookTryUseTotem(LivingEntity livingEntity, Hand hand) {
        return UseTotemFromInventory.tryFromInventory(livingEntity, hand);
    }

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
        method = "getArmorVisibility",
        at = @At(value = "RETURN"),
        cancellable = true
    )
    private void hookArmorCover(CallbackInfoReturnable<Float> cir) {
        if (Meson.enabled("charm:armor_invisibility")) {
            LivingEntity entity = (LivingEntity) (Object) this;
            Iterable<ItemStack> armorItems = this.getArmorItems();

            int i = 0;
            int j = 0;

            for (ItemStack itemstack : armorItems) {
                if (!ArmorInvisibility.shouldArmorBeInvisible(entity, itemstack)) {
                    ++j;
                }
                ++i;
            }

            cir.setReturnValue(i > 0 ? (float)j / (float)i : 0.0F);
        }
    }
}
