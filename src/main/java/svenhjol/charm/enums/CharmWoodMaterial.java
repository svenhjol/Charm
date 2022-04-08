package svenhjol.charm.enums;

import net.minecraft.util.RandomSource;

import java.util.List;

public enum CharmWoodMaterial implements IWoodMaterial {
    AZALEA;

    public static IWoodMaterial getRandomMaterial(RandomSource random) {
        List<IWoodMaterial> types = VanillaWoodMaterial.getTypes();
        types.add(AZALEA);
        return types.get(random.nextInt(types.size()));
    }
}
