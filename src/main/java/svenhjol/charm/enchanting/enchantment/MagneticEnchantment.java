package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.MesonModule;

public class MagneticEnchantment extends MesonEnchantment {
    public MagneticEnchantment(MesonModule module) {
        super(module, "magnetic",
            Rarity.UNCOMMON, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel) {
        return 15;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    @Override
    public boolean isTreasureEnchantment() {
        return false;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() instanceof ShearsItem || super.canApply(stack);
    }
}
