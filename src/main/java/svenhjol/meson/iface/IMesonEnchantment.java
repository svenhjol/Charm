package svenhjol.meson.iface;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonEnchantment
{
    default void register(ResourceLocation res)
    {
        RegistryHandler.registerEnchantment((Enchantment)this, res);
    }
}
