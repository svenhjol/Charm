package svenhjol.charm.module.aerial_affinity_enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.enchantment.CharmEnchantment;

public class AerialAffinityEnch extends CharmEnchantment {
    public AerialAffinityEnch(CharmModule module) {
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
