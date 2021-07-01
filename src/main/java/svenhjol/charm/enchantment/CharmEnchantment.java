package svenhjol.charm.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import svenhjol.charm.loader.CharmCommonModule;

public abstract class CharmEnchantment extends Enchantment implements ICharmEnchantment {
    protected CharmCommonModule module;

    public CharmEnchantment(CharmCommonModule module, String name, Rarity rarity, EnchantmentCategory target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
        this.register(module, name);
        this.module = module;
    }

    public boolean enabled() {
        return module.isEnabled();
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return module.isEnabled() && super.canEnchant(stack);
    }

    @Override
    public boolean isTradeable() {
        return module.isEnabled() && super.isTradeable();
    }

    @Override
    public boolean isDiscoverable() {
        return module.isEnabled() && super.isDiscoverable();
    }
}
