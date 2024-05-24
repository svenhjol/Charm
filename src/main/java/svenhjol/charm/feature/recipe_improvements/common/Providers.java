package svenhjol.charm.feature.recipe_improvements.common;

import svenhjol.charm.api.iface.ConditionalAdvancement;
import svenhjol.charm.api.iface.ConditionalAdvancementProvider;
import svenhjol.charm.api.iface.ConditionalRecipe;
import svenhjol.charm.api.iface.ConditionalRecipeProvider;
import svenhjol.charm.feature.recipe_improvements.RecipeImprovements;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public class Providers extends ProviderHolder<RecipeImprovements> implements ConditionalRecipeProvider, ConditionalAdvancementProvider {
    public Providers(RecipeImprovements feature) {
        super(feature);
    }

    @Override
    public List<ConditionalRecipe> getRecipeConditions() {
        var prefix = feature().snakeCaseName() + "/";

        return List.of(
            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().rawOreBlocks();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "*from_blasting_raw_*_block");
                }
            },

            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().gildedBlackstone();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "gilded_blackstone");
                }
            },

            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().snowballs();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "snowballs_from_snow_block");
                }
            },

            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().quartz();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "quartz_from_quartz_block");
                }
            },

            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().clay();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "clay_balls_from_clay_block");
                }
            },

            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().cyanDye();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "cyan_dye");
                }
            },

            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().greenDye();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "green_dye");
                }
            },

            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().soulTorch();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + "soul_torch");
                }
            }
        );
    }

    @Override
    public List<ConditionalAdvancement> getAdvancementConditions() {
        return feature().handlers.getAdvancementConditions(feature(), this);
    }
}
