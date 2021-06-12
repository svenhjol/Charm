package svenhjol.charm.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum VanillaMetalMaterial implements IMetalMaterial {
    COPPER,
    GOLD,
    NETHERITE;

    public static List<IMetalMaterial> getTypes() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    public static List<IMetalMaterial> getTypesWithout(IMetalMaterial... types) {
        List<IMetalMaterial> typesList = new ArrayList<>(Arrays.asList(types));
        return Arrays.stream(values()).filter(t -> !typesList.contains(t)).collect(Collectors.toList());
    }
}
