package svenhjol.charm.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import svenhjol.charm.Charm;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.firing.Firing;
import svenhjol.charm.feature.kilns.Kilns;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charm.feature.woodcutting.Woodcutting;

public final class Definitions {
    public static final CategoryIdentifier<WoodcuttingDisplay> WOODCUTTING
        = CategoryIdentifier.of(Charm.ID, "plugins/woodcutting");

    public static final CategoryIdentifier<FiringDisplay> FIRING
        = CategoryIdentifier.of(Charm.ID, "plugins/firing");

    public static Firing firing() {
        return Resolve.feature(Firing.class);
    }

    public static Kilns kilns() {
        return Resolve.feature(Kilns.class);
    }

    public static Woodcutters woodcutters() {
        return Resolve.feature(Woodcutters.class);
    }

    public static Woodcutting woodcutting() {
        return Resolve.feature(Woodcutting.class);
    }
}
