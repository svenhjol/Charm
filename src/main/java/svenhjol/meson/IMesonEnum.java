package svenhjol.meson;

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
}
