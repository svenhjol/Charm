package svenhjol.charm.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.RegistryHelper;

public interface ICharmEnchantment {
    default void register(CharmModule module, String name) {
        RegistryHelper.enchantment(new Identifier(module.mod, name), (Enchantment)this);
    }
}
