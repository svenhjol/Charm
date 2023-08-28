package svenhjol.charm.mixin.enchantable_horse_armor;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Horse.class)
public class HorseMixin extends AbstractHorse {
    protected HorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return super.getItemBySlot(EquipmentSlot.CHEST);
    }
}