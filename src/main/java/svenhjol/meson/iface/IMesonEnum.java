package svenhjol.meson.iface;

import net.minecraft.util.IStringSerializable;

import java.util.Locale;

@SuppressWarnings("unused")
public interface IMesonEnum extends IStringSerializable {
    @Override
    @SuppressWarnings("rawtypes")
    default String getName() {
        return ((Enum) this).name().toLowerCase(Locale.ENGLISH);
    }

    default String getCapitalizedName() {
        String name = getName();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
