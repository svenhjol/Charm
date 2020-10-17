package svenhjol.charm.base.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum VanillaVariantMaterial implements IVariantMaterial {
    OAK,
    SPRUCE,
    BIRCH,
    JUNGLE,
    ACACIA,
    DARK_OAK,
    CRIMSON,
    WARPED;

    public static List<IVariantMaterial> getTypes() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    public static List<IVariantMaterial> getTypesWithout(IVariantMaterial... types) {
        List<IVariantMaterial> typesList = new ArrayList<>(Arrays.asList(types));
        return Arrays.stream(values()).filter(t -> !typesList.contains(t)).collect(Collectors.toList());
    }

    @Override
    public boolean isFlammable() {
        // non_flammable_wood is not available at reg point
        return !this.equals(CRIMSON) && !this.equals(WARPED);
    }
}
