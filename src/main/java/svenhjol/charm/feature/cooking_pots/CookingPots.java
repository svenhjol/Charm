package svenhjol.charm.feature.cooking_pots;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.atlases.common.Item;
import svenhjol.charm.feature.cooking_pots.common.Advancements;
import svenhjol.charm.feature.cooking_pots.common.Handlers;
import svenhjol.charm.feature.cooking_pots.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Cooking pots allow any food item to be added. Once the combined nourishment total has reached maximum, use wooden bowls to take mixed stew from the pot.
    All negative and positive effects will be removed from the food added to the pot.""")
public final class CookingPots extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    @Configurable(
        name = "Hunger restored",
        description = """
            Number of hunger points restored from a single portion of mixed stew."""
    )
    private static int hungerPerStew = 5;

    @Configurable(
        name = "Saturation restored",
        description = """
            Amount of saturation restored from a single portion of mixed stew."""
    )
    private static double saturationPerStew = 0.5f;

    @Configurable(
        name = "Mixed stew stack size",
        description = """
            Maximum stack size of stew obtained from the cooking pot."""
    )
    private static int stewStackSize = 64;

    public CookingPots(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }

    public int hungerPerStew() {
        return Mth.clamp(hungerPerStew, 0, 64);
    }

    public float saturationPerStew() {
        return (float)Mth.clamp(saturationPerStew, 0.0f, 64.0f);
    }

    public int stewStackSize() {
        return Mth.clamp(stewStackSize, 1, Item.DEFAULT_MAX_STACK_SIZE);
    }
}
