package svenhjol.charm.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public enum VanillaWoodMaterial implements IWoodMaterial {
    ACACIA(true),
    BIRCH(true),
    CRIMSON(false),
    DARK_OAK(true),
    JUNGLE(true),
    MANGROVE(true),
    OAK(true),
    SPRUCE(true),
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
