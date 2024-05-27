package svenhjol.charm.feature.cooking_pots.common;

import net.minecraft.util.StringRepresentable;

public enum CookingStatus implements StringRepresentable {
    EMPTY("empty"),
    FILLED_WITH_WATER("filled_with_water"),
    HAS_SOME_FOOD("has_some_food"),
    COOKED("cooked");

    private final String name;

    CookingStatus(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}
