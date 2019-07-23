package svenhjol.meson.iface;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.registry.ProxyRegistry;

public interface IMesonItem
{
    String getModId();

    default void register(String name)
    {
        Item item = (Item)this;

        ResourceLocation res = new ResourceLocation(getModId(), name);
        item.setRegistryName(res);

        ProxyRegistry.register(item);
        ProxyRegistry.ITEMS.add(item);
    }
}
