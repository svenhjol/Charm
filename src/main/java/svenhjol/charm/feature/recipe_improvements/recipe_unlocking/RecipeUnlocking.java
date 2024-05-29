package svenhjol.charm.feature.recipe_improvements.recipe_unlocking;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.charmony.feature.ChildFeature;
import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.feature.recipe_improvements.recipe_unlocking.common.Handlers;
import svenhjol.charm.feature.recipe_improvements.recipe_unlocking.common.Registers;

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
