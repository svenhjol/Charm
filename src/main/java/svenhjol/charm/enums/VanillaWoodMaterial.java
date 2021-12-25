package svenhjol.charm.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public enum VanillaWoodMaterial implements IWoodMaterial {
    OAK(true),
    SPRUCE(true),
    BIRCH(true),
    JUNGLE(true),
    ACACIA(true),
    DARK_OAK(true),
    CRIMSON(false),
    WARPED(false);

    private final boolean flammable;

    VanillaWoodMaterial(boolean flammable) {
        this.flammable = flammable;
    }

    @Override
    public boolean isFlammable() {
        return flammable;
    }

    public static List<IWoodMaterial> getTypes() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    public static List<IWoodMaterial> getTypesWithout(IWoodMaterial... types) {
        List<IWoodMaterial> typesList = new ArrayList<>(Arrays.asList(types));
        return Arrays.stream(values()).filter(t -> !typesList.contains(t)).collect(Collectors.toList());
    }
}
