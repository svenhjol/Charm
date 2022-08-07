package svenhjol.charm.enums;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public enum CharmWoodMaterial implements IWoodMaterial {
    AZALEA,
    EBONY;

    public static IWoodMaterial getRandomMaterial(Random random) {
        List<IWoodMaterial> types = VanillaWoodMaterial.getTypes();
        types.addAll(Arrays.asList(AZALEA, EBONY));
        return types.get(random.nextInt(types.size()));
    }
}
