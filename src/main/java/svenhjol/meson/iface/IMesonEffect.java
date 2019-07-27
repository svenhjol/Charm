package svenhjol.meson.iface;

import svenhjol.meson.handler.RegistrationHandler;

public interface IMesonEffect
{
    String getModId();

    String getBaseName();

    default void register()
    {
        RegistrationHandler.addEffect(this);
    }
}
