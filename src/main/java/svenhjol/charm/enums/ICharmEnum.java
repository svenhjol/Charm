package svenhjol.charm.enums;

import net.minecraft.util.StringRepresentable;

import java.util.Locale;

@SuppressWarnings({"NullableProblems", "rawtypes", "unused"})
public interface ICharmEnum extends StringRepresentable {
    @Override
    default String getSerializedName() {
        return ((Enum)this).name().toLowerCase(Locale.ENGLISH);
    }

    default String getNameAsString() {
        return ((Enum)this).name();
    }

    default String getCapitalizedName() {
        String name = getSerializedName();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
