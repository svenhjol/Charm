package svenhjol.charm.mixin.enchantable_horse_armor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.enchantable_horse_armor.EnchantableHorseArmor;

@Mixin(Horse.class)
public class HorseMixin extends AbstractHorse {
    protected HorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return super.getItemBySlot(EquipmentSlot.CHEST);
    }

    /**
     * This is used to trigger the advancement.
     */
    @Inject(
        method = "setArmor",
        at = @At("TAIL")
    )
    private void hookSetArmor(ItemStack itemStack, CallbackInfo ci) {
        if (level() instanceof ServerLevel serverLevel && itemStack.isEnchanted()) {
            EnchantableHorseArmor.triggerAddEnchantmentToHorseArmor(serverLevel, blockPosition());
        }
    }
}