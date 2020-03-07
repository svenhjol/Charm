package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.MesonModule;

public class CurseBreakEnchantment extends MesonEnchantment {
    public CurseBreakEnchantment(MesonModule module) {
        super(module, "curse_break",
            Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.MAINHAND);
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        return stack.getItem() == Items.BOOK;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
