package svenhjol.charm.enums;

import java.util.Locale;
import net.minecraft.util.StringRepresentable;

@SuppressWarnings({"NullableProblems", "rawtypes"})
public interface ICharmEnum extends StringRepresentable {
    @Override
    default String getSerializedName() {
        return ((Enum)this).name().toLowerCase(Locale.ENGLISH);
    }

    default String getCapitalizedName() {
        String name = getSerializedName();
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }
}
