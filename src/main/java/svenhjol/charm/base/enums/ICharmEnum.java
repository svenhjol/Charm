package svenhjol.charm.base.enums;

import net.minecraft.util.StringIdentifiable;

import java.util.Locale;

@SuppressWarnings({"NullableProblems", "rawtypes"})
public interface ICharmEnum extends StringIdentifiable {
    @Override
    default String asString() {
        return ((Enum)this).name().toLowerCase(Locale.ENGLISH);
    }

    default String getCapitalizedName() {
        String name = asString();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
