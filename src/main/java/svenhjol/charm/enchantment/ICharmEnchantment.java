package svenhjol.charm.enchantment;

import svenhjol.charm.module.CharmModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.helper.RegistryHelper;

public interface ICharmEnchantment {
    default void register(CharmModule module, String name) {
        RegistryHelper.enchantment(new ResourceLocation(module.mod, name), (Enchantment)this);
    }
}
