package svenhjol.charm.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enchantment.CharmEnchantment;

public class AerialAffinityEnchantment extends CharmEnchantment {
    public AerialAffinityEnchantment(CharmModule module) {
        super(module, "aerial_affinity", Rarity.RARE, EnchantmentTarget.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
    }

    @Override
    public int getMinPower(int level) {
        return 1;
    }

    @Override
    public int getMaxPower(int enchantmentLevel) {
        return this.getMinPower(enchantmentLevel) + 40;
    }

    @Override
    public boolean isTreasure() {
        return false;
    }
}
