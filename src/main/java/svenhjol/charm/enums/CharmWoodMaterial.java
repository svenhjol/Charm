package svenhjol.charm.enums;

import java.util.List;
import java.util.Random;

public enum CharmWoodMaterial implements IWoodMaterial {
    AZALEA;

    public static IWoodMaterial getRandomMaterial(Random random) {
        List<IWoodMaterial> types = VanillaWoodMaterial.getTypes();
        types.add(AZALEA);
        return types.get(random.nextInt(types.size()));
    }
}
