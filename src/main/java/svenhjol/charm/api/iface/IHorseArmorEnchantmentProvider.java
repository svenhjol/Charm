package svenhjol.charm.api.iface;

import net.minecraft.world.item.enchantment.Enchantment;

import java.util.List;

/**
 * Add custom enchantments to horse armor via Charm's EnchantableHorseArmor feature.
 */
@SuppressWarnings("unused")
public interface IHorseArmorEnchantmentProvider {
    List<Enchantment> getEnchantments();
}
