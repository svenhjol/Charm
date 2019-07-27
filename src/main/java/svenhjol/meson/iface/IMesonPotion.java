package svenhjol.meson.iface;

import net.minecraft.item.crafting.Ingredient;
import svenhjol.meson.handler.RegistrationHandler;

public interface IMesonPotion
{
    String getModId();

    String getBaseName();

    default void register()
    {
        RegistrationHandler.addPotion(this);
    }

    Ingredient getBase();

    Ingredient getReagant();
}
