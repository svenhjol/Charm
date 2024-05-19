package svenhjol.charm.feature.recipe_improvements.common;

import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public class Providers extends ProviderHolder<RecipeImprovements> implements IConditionalRecipeProvider, IConditionalAdvancementProvider {
    public Providers(RecipeImprovements feature) {
        super(feature);
    }

    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        var prefix = feature().snakeCaseName() + "/";

        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return RecipeImprovements.rawOreBlocks;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "*from_blasting_raw_*_block");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return RecipeImprovements.gildedBlackstone;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "gilded_blackstone");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return RecipeImprovements.snowballs;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "snowballs_from_snow_block");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return RecipeImprovements.quartz;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "quartz_from_quartz_block");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return RecipeImprovements.clay;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "clay_balls_from_clay_block");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return RecipeImprovements.cyanDye;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "cyan_dye");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return RecipeImprovements.greenDye;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "green_dye");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return RecipeImprovements.soulTorch;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "soul_torch");
                }
            }
        );
    }

    @Override
    public List<IConditionalAdvancement> getAdvancementConditions() {
        return feature().handlers.getAdvancementConditions(feature(), this);
    }
}
