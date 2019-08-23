package svenhjol.meson.iface;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.MesonModule;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonItem
{
    boolean isEnabled();

    default void register(MesonModule module, String name)
    {
        RegistryHandler.registerItem((Item)this, new ResourceLocation(module.mod, name));
    }
}
