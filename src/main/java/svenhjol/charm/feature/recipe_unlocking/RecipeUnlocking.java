package svenhjol.charm.feature.recipe_unlocking;

import svenhjol.charm.feature.recipe_unlocking.common.Handlers;
import svenhjol.charm.feature.recipe_unlocking.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(enabledByDefault = false, description = """
    Unlocks all vanilla recipes.
    This opinionated feature is disabled by default.""")
public final class RecipeUnlocking extends CommonFeature {
    public final Registers registers;
    public final Handlers handlers;

    public RecipeUnlocking(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }
}
