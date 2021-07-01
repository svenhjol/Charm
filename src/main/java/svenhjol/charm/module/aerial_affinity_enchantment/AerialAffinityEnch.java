package svenhjol.charm.module.aerial_affinity_enchantment;

import svenhjol.charm.loader.CommonModule;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import svenhjol.charm.enchantment.CharmEnchantment;

public class AerialAffinityEnch extends CharmEnchantment {
    public AerialAffinityEnch(CommonModule module) {
        super(module, "aerial_affinity", Rarity.RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
    }

    @Override
    public int getMinCost(int level) {
        return 1;
    }

    @Override
    public int getMaxCost(int enchantmentLevel) {
        return this.getMinCost(enchantmentLevel) + 40;
    }

    @Override
    public boolean isTreasureOnly() {
        return false;
    }
}
