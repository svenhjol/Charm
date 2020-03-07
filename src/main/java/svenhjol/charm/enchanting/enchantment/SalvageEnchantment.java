package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.MesonModule;

public class SalvageEnchantment extends MesonEnchantment {
    public SalvageEnchantment(MesonModule module) {
        super(module, "salvage",
            Rarity.UNCOMMON, EnchantmentType.BREAKABLE, EquipmentSlotType.MAINHAND);
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 5;
    }
}
