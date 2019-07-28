package svenhjol.meson.iface;

import svenhjol.meson.handler.RegistrationHandler;

public interface IMesonPotion
{
    String getModId();

    String getBaseName();

    default void register()
    {
        RegistrationHandler.addPotion(this);
    }

    void registerRecipe();
}
