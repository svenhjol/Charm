package svenhjol.meson.iface;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonEnchantment {
    default void register(MesonModule module, String name) {
        RegistryHandler.registerEnchantment((Enchantment) this, new ResourceLocation(module.mod, name));
    }
}
