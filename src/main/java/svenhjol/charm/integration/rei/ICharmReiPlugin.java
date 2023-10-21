package svenhjol.charm.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import svenhjol.charm.Charm;

/**
 * See me.shedaniel.rei.plugin.common.BuiltinPlugin for reference implementation.
 */
public interface ICharmReiPlugin {
    CategoryIdentifier<WoodcuttingDisplay> WOODCUTTING
        = CategoryIdentifier.of(Charm.ID, "plugins/woodcutting");

    CategoryIdentifier<FiringDisplay> FIRING
        = CategoryIdentifier.of(Charm.ID, "plugins/firing");
}
