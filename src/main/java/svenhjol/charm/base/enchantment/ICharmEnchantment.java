package svenhjol.charm.base.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.Identifier;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.RegistryHandler;

public interface ICharmEnchantment {
    default void register(CharmModule module, String name) {
        RegistryHandler.enchantment(new Identifier(module.mod, name), (Enchantment)this);
    }
}
