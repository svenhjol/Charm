package svenhjol.charm.enums;

import net.minecraft.util.RandomSource;

import java.util.Arrays;
import java.util.List;

public enum CharmWoodMaterial implements IWoodMaterial {
    AZALEA,
    EBONY;

    public static IWoodMaterial getRandomMaterial(RandomSource random) {
        List<IWoodMaterial> types = VanillaWoodMaterial.getTypes();
        types.addAll(Arrays.asList(AZALEA, EBONY));
        return types.get(random.nextInt(types.size()));
    }
}
