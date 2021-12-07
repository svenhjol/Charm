package svenhjol.charm.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum VanillaWoodMaterial implements IWoodMaterial {
    OAK,
    SPRUCE,
    BIRCH,
    JUNGLE,
    ACACIA,
    DARK_OAK,
    CRIMSON,
    WARPED;

    public static List<IWoodMaterial> getTypes() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    public static List<IWoodMaterial> getTypesWithout(IWoodMaterial... types) {
        List<IWoodMaterial> typesList = new ArrayList<>(Arrays.asList(types));
        return Arrays.stream(values()).filter(t -> !typesList.contains(t)).collect(Collectors.toList());
    }

    @Override
    public boolean isFlammable() {
        // TODO: non_flammable_wood is not available at reg point
        return !this.equals(CRIMSON) && !this.equals(WARPED);
    }
}
