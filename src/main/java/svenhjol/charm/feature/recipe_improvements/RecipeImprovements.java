package svenhjol.charm.feature.recipe_improvements;

import svenhjol.charm.feature.recipe_improvements.bundles_from_leather.BundlesFromLeather;
import svenhjol.charm.feature.recipe_improvements.common.Handlers;
import svenhjol.charm.feature.recipe_improvements.common.Providers;
import svenhjol.charm.feature.recipe_improvements.recipe_unlocking.RecipeUnlocking;
import svenhjol.charm.feature.recipe_improvements.shapeless_recipes.ShapelessRecipes;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.feature.ChildFeature;

import java.util.List;

@Feature(description = """
    Adds more recipes and recipe tweaks.
    Disabling this feature will disable all related recipe features.""")
public final class RecipeImprovements extends CommonFeature {
    public final Handlers handlers;
    public final Providers providers;

    @Configurable(name = "Ore block from raw ore block", description = "If true, adds a blast furnace recipe for smelting raw ore blocks into ore blocks.")
    private static boolean rawOreBlocks = true;

    @Configurable(name = "Gilded Blackstone", description = "If true, adds a recipe for Gilded Blackstone using gold nuggets and blackstone.")
    private static boolean gildedBlackstone = true;

    @Configurable(name = "Cyan Dye from warped roots", description = "If true, adds a recipe for Cyan Dye using warped roots.")
    private static boolean cyanDye = true;

    @Configurable(name = "Green Dye from yellow and blue", description = "If true, adds a recipe for Green Dye using yellow and blue dyes.")
    private static boolean greenDye = true;

    @Configurable(name = "Snowballs from snow blocks", description = "If true, adds a recipe for turning snow blocks back into snowballs.")
    private static boolean snowballs = true;

    @Configurable(name = "Quartz from quartz blocks", description = "If true, adds a recipe for turning quartz blocks back into quartz.")
    private static boolean quartz = true;

    @Configurable(name = "Clay balls from clay blocks", description = "If true, adds a recipe for turning clay blocks back into clay balls.")
    private static boolean clay = true;

    @Configurable(name = "Simpler Soul Torch", description = "If true, adds a recipe for Soul Torches using soul sand/soul soil and sticks.")
    private static boolean soulTorch = true;

    public RecipeImprovements(CommonLoader loader) {
        super(loader);

        handlers = new Handlers(this);
        providers = new Providers(this);
    }

    public boolean rawOreBlocks() {
        return rawOreBlocks;
    }

    public boolean gildedBlackstone() {
        return gildedBlackstone;
    }

    public boolean cyanDye() {
        return cyanDye;
    }

    public boolean greenDye() {
        return greenDye;
    }

    public boolean snowballs() {
        return snowballs;
    }

    public boolean quartz() {
        return quartz;
    }

    public boolean clay() {
        return clay;
    }

    public boolean soulTorch() {
        return soulTorch;
    }


    @Override
    public List<? extends ChildFeature<? extends svenhjol.charm.foundation.Feature>> children() {
        return List.of(
            new BundlesFromLeather(loader()),
            new RecipeUnlocking(loader()),
            new ShapelessRecipes(loader())
        );
    }
}
