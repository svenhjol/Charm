package svenhjol.charm.feature.recipe_improvements.recipe_unlocking.common;

import svenhjol.charm.api.event.PlayerLoginEvent;
import svenhjol.charm.feature.recipe_improvements.recipe_unlocking.RecipeUnlocking;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<RecipeUnlocking> {
    public Registers(RecipeUnlocking feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(feature().handlers::playerLogin);
    }
}
