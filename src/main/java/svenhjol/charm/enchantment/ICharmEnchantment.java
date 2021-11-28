package svenhjol.charm.enchantment;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.registry.CommonRegistry;
import svenhjol.charm.loader.CharmModule;

public interface ICharmEnchantment {
    default void register(CharmModule module, String name) {
        CommonRegistry.enchantment(new ResourceLocation(module.getModId(), name), (Enchantment)this);
    }
}
