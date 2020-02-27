package svenhjol.meson;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import svenhjol.meson.iface.IMesonEnchantment;

public abstract class MesonEnchantment extends Enchantment implements IMesonEnchantment
{
    protected MesonModule module;

    public MesonEnchantment(MesonModule module, String name, Rarity rarity, EnchantmentType type, EquipmentSlotType... slots)
    {
        super(rarity, type, slots);
        register(module, name);
        this.module = module;
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        return module.enabled && super.canApply(stack);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack)
    {
        return module.enabled && super.canApplyAtEnchantingTable(stack);
    }

    @Override
    public boolean isAllowedOnBooks()
    {
        return module.enabled && super.isAllowedOnBooks();
    }
}
