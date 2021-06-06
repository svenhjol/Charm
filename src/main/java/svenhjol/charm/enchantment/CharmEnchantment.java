package svenhjol.charm.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import svenhjol.charm.enchantment.ICharmEnchantment;
import svenhjol.charm.module.CharmModule;

public abstract class CharmEnchantment extends Enchantment implements ICharmEnchantment {
    protected CharmModule module;

    public CharmEnchantment(CharmModule module, String name, Rarity rarity, EnchantmentCategory target, EquipmentSlot[] slotTypes) {
        super(rarity, target, slotTypes);
        this.register(module, name);
        this.module = module;
    }

    public boolean enabled() {
        return module.enabled;
    }

    @Override
    public boolean canEnchant(ItemStack stack) {
        return module.enabled && super.canEnchant(stack);
    }

    @Override
    public boolean isTradeable() {
        return module.enabled && super.isTradeable();
    }

    @Override
    public boolean isDiscoverable() {
        return module.enabled && super.isDiscoverable();
    }
}
