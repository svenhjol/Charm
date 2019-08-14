package svenhjol.meson.iface;

import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.handler.RegistryHandler;

public interface IMesonPotion
{
    void registerRecipe();

    default void register(ResourceLocation res)
    {
        RegistryHandler.registerPotion((Potion)this, res);
    }
}
