package svenhjol.meson.iface;

import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Locale;

public interface IMesonEnum extends IStringSerializable
{
    @Override
    @Nonnull
    default String getName()
    {
        return ((Enum)this).name().toLowerCase(Locale.ENGLISH);
    }

    default String getCapitalizedName()
    {
        String name = getName();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
