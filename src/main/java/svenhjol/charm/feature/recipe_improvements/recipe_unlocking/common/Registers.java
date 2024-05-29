package svenhjol.charm.feature.recipe_improvements.recipe_unlocking.common;

import svenhjol.charm.api.event.PlayerLoginEvent;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.recipe_improvements.recipe_unlocking.RecipeUnlocking;

public final class Registers extends RegisterHolder<RecipeUnlocking> {
    public Registers(RecipeUnlocking feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        PlayerLoginEvent.INSTANCE.handle(feature().handlers::playerLogin);
    }
}
