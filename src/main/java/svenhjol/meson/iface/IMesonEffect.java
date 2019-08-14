package svenhjol.meson.iface;

import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonEffect
{
    default void register(ResourceLocation res)
    {
        RegistryHandler.registerEffect((Effect)this, res);
    }
}
