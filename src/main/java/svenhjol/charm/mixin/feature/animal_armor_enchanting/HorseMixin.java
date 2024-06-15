package svenhjol.charm.mixin.feature.animal_armor_enchanting;

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
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.animal_armor_enchanting.AnimalArmorEnchanting;

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
            Resolve.feature(AnimalArmorEnchanting.class).advancements.addedEnchantmentToArmor(serverLevel, blockPosition());
        }
    }
}