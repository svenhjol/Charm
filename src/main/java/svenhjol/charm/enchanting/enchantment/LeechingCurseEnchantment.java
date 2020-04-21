package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.MesonModule;

public class LeechingCurseEnchantment extends MesonEnchantment {
    public LeechingCurseEnchantment(MesonModule module) {
        super(module, "leeching_curse",
            Rarity.UNCOMMON, EnchantmentType.BREAKABLE, EquipmentSlotType.values());
    }

    @Override
    public boolean isCurse() {
        return true;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 25;
    }

    @Override
    public int getMaxEnchantability(int enchantmentLevel) {
        return 50;
    }
}
