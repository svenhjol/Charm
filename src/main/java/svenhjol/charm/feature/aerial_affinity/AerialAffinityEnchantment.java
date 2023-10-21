package svenhjol.charm.feature.aerial_affinity;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import svenhjol.charmony.base.CharmonyEnchantment;
import svenhjol.charmony.common.CommonFeature;

public class AerialAffinityEnchantment extends CharmonyEnchantment {
    public AerialAffinityEnchantment(CommonFeature feature) {
        super(feature, Rarity.RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
    }
    
    @Override
    public int getMinCost(int level) {
        return 1;
    }
    
    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 40;
    }
}