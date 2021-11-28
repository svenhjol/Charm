package svenhjol.charm.enchantment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.helper.RegistryHelper;
import svenhjol.charm.loader.CharmModule;

public interface ICharmEnchantment {
    default void register(CharmModule module, String name) {
        RegistryHelper.enchantment(new ResourceLocation(module.getModId(), name), (Enchantment)this);
    }
}
