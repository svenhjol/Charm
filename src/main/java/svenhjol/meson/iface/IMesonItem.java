package svenhjol.meson.iface;

import svenhjol.meson.handler.RegistrationHandler;

public interface IMesonItem
{
    String getModId();

    String getBaseName();

    default void register()
    {
        RegistrationHandler.addItem(this);
    }
}
