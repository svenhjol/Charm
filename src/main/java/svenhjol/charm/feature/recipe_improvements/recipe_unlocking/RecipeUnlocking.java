package svenhjol.charm.feature.recipe_improvements.recipe_unlocking;

import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.feature.recipe_improvements.recipe_unlocking.common.Handlers;
import svenhjol.charm.feature.recipe_improvements.recipe_unlocking.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

@Feature(enabledByDefault = false, description = """
    Unlocks all vanilla recipes when the player joins the game.
    This feature may cause problems with mod progression and is disabled by default.""")
public final class RecipeUnlocking extends CommonFeature implements ChildFeature<RecipeImprovements> {
    public final Registers registers;
    public final Handlers handlers;

    public RecipeUnlocking(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public Class<RecipeImprovements> typeForParent() {
        return RecipeImprovements.class;
    }
}
