package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShearsItem;
import net.minecraft.util.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonEnchantment;

public class MagneticEnchantment extends MesonEnchantment
{
    public MagneticEnchantment()
    {
        super(Rarity.UNCOMMON, EnchantmentType.DIGGER, EquipmentSlotType.MAINHAND);

        register(new ResourceLocation(Charm.MOD_ID, "magnetic"));
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 15;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }

    @Override
    public boolean isTreasureEnchantment()
    {
        return false;
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        return stack.getItem() instanceof ShearsItem || super.canApply(stack);
    }
}
