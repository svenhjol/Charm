package svenhjol.meson.iface;

import net.minecraft.util.IStringSerializable;

@SuppressWarnings("unused")
public interface IMesonEnum extends IStringSerializable
{
    @Override
    @SuppressWarnings("rawtypes")
    default String getName()
    {
        return ((Enum)this).name().toLowerCase();
    }

    default String getCapitalizedName()
    {
        String name = getName();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
