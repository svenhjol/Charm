package svenhjol.meson.iface;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.registry.ProxyRegistry;

public interface IMesonPotion
{
    default void register(String name)
    {
        Potion self = (Potion) this;

        self.setPotionName(name);
        self.setRegistryName(new ResourceLocation(getModId(), name));

        ProxyRegistry.register(self);
    }

    String getModId();
}
