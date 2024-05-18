package svenhjol.charm.mixin.feature.animal_armor_enchanting;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;
import svenhjol.charm.foundation.Resolve;

@SuppressWarnings("UnreachableCode")
@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    @Shadow public abstract ItemStack getBodyArmorItem();

    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    /**
     * Always return the animal's body armor when a slot is requested.
     */
    @Inject(
        method = "getItemBySlot",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetItemBySlot(EquipmentSlot equipmentSlot, CallbackInfoReturnable<ItemStack> cir) {
        var mob = (Mob)(Object)(this);
        if ((mob instanceof Wolf || mob instanceof Horse)) {
            cir.setReturnValue(getBodyArmorItem());
        }
    }

    /**
     * This is used to trigger the advancement.
     */
    @Inject(
        method = "setBodyArmorItem",
        at = @At("TAIL")
    )
    private void hookSetArmor(ItemStack stack, CallbackInfo ci) {
        var mob = (Mob)(Object)(this);
        if ((mob instanceof Wolf || mob instanceof Horse) && level() instanceof ServerLevel serverLevel && stack.isEnchanted()) {
            Resolve.feature(AnimalArmorEnchanting.class).advancements.addedEnchantmentToArmor(serverLevel, blockPosition());
        }
    }
}