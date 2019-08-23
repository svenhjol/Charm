package svenhjol.meson.iface;

import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonEffect
{
    default void register(MesonModule module, String name)
    {
        RegistryHandler.registerEffect((Effect)this, new ResourceLocation(module.mod, name));
    }
}
