package svenhjol.charm.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enchantment.CharmEnchantment;

public class TintedEnchantment extends CharmEnchantment {
    public TintedEnchantment(CharmModule module) {
        super(module, "tinted", Rarity.RARE, EnchantmentTarget.BREAKABLE, EquipmentSlot.values());
    }
}
