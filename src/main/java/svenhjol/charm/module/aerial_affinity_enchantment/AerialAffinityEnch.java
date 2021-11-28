package svenhjol.charm.module.aerial_affinity_enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import svenhjol.charm.enchantment.CharmEnchantment;
import svenhjol.charm.loader.CharmModule;

public class AerialAffinityEnch extends CharmEnchantment {
    public AerialAffinityEnch(CharmModule module) {
        super(module, "aerial_affinity", Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[] { EquipmentSlot.FEET });
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
