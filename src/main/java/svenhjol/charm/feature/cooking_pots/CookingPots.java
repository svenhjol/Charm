package svenhjol.charm.feature.cooking_pots;

import net.minecraft.util.Mth;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.atlases.common.Item;
import svenhjol.charm.feature.cooking_pots.common.Advancements;
import svenhjol.charm.feature.cooking_pots.common.Handlers;
import svenhjol.charm.feature.cooking_pots.common.Registers;

@Feature(description = """
    Cooking pots allow any food item to be added. Once the combined nourishment total has reached maximum, use wooden bowls to take mixed stew from the pot.
    All negative and positive effects will be removed from the food added to the pot.""")
public final class CookingPots extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    @Configurable(
        name = "Stew hunger restored",
        description = """
            Number of hunger points restored from a single portion of mixed stew."""
    )
    private static int stewHungerRestored = 6;

    @Configurable(
        name = "Stew saturation restored",
        description = """
            Amount of saturation restored from a single portion of mixed stew."""
    )
    private static double stewSaturationRestored = 0.6d;

    @Configurable(
        name = "Stew stack size",
        description = """
            Maximum stack size of stew obtained from the cooking pot."""
    )
    private static int stewStackSize = 16;

    public CookingPots(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }

    public int stewHungerRestored() {
        return Mth.clamp(stewHungerRestored, 0, 64);
    }

    public float stewSaturationRestored() {
        return (float)Mth.clamp(stewSaturationRestored, 0.0f, 64.0f);
    }

    public int stewStackSize() {
        return Mth.clamp(stewStackSize, 1, Item.DEFAULT_MAX_STACK_SIZE);
    }
}
