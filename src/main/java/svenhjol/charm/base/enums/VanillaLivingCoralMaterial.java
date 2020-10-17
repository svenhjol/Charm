package svenhjol.charm.base.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum VanillaLivingCoralMaterial implements ICoralMaterial {
    TUBE,
    BRAIN,
    BUBBLE,
    FIRE,
    HORN;

    public static List<ICoralMaterial> getTypes() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }
}
