package svenhjol.meson.iface;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonItem
{
    default void register(ResourceLocation res)
    {
        RegistryHandler.registerItem((Item)this, res);
    }
}
