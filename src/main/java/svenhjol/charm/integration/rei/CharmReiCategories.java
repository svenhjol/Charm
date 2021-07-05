package svenhjol.charm.integration.rei;

import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import svenhjol.charm.Charm;

public class CharmReiCategories {
    public static final CategoryIdentifier<WoodcuttingDisplay> WOODCUTTING = CategoryIdentifier.of(Charm.MOD_ID, "plugins/woodcutting");
    public static final CategoryIdentifier<FiringDisplay> FIRING = CategoryIdentifier.of(Charm.MOD_ID, "plugins/firing");
}
