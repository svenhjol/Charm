package svenhjol.charm.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum VanillaMetalMaterial implements IMetalMaterial {
    IRON(5.0F, 6.0F, true, false),
    COPPER(3.5F, 6.0F, false, false),
    GOLD(3.5F, 3.5F, true, false),
    NETHERITE(50.0F, 2500.0F, false, true);

    private final float destroyTime;
    private final float resistance;
    private final boolean immuneToLava;
    private final boolean hasNuggets;

    VanillaMetalMaterial(float destroyTime, float resistance, boolean hasNuggets, boolean immuneToLava) {
        this.destroyTime = destroyTime;
        this.resistance = resistance;
        this.hasNuggets = hasNuggets;
        this.immuneToLava = immuneToLava;
    }

    @Override
    public float getDestroyTime() {
        return destroyTime;
    }

    @Override
    public float getResistance() {
        return resistance;
    }

    @Override
    public boolean isFireResistant() {
        return immuneToLava;
    }

    @Override
    public boolean hasNuggets() {
        return hasNuggets;
    }

    public static List<IMetalMaterial> getTypes() {
        return Arrays.stream(values()).collect(Collectors.toList());
    }

    public static List<IMetalMaterial> getTypesWithout(IMetalMaterial... types) {
        List<IMetalMaterial> typesList = new ArrayList<>(Arrays.asList(types));
        return Arrays.stream(values()).filter(t -> !typesList.contains(t)).collect(Collectors.toList());
    }
}
