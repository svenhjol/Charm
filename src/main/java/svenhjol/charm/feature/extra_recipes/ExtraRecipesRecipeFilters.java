package svenhjol.charm.feature.extra_recipes;

import svenhjol.charmony_api.iface.IRecipeFilter;
import svenhjol.charmony_api.iface.IRecipeRemoveProvider;

import java.util.List;

public class ExtraRecipesRecipeFilters implements IRecipeRemoveProvider {
    static final String PREFIX = "extra_recipes/";
    @Override
    public List<IRecipeFilter> getRecipeFilters() {
        return List.of(
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.rawOreBlocks;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "*from_blasting_raw_*_block");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.gildedBlackstone;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "gilded_blackstone");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.snowballs;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "snowballs_from_snow_block");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.quartz;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "quartz_from_quartz_block");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.clay;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "clay_balls_from_clay_block");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.cyanDye;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "cyan_dye");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.greenDye;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "green_dye");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.soulTorch;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "soul_torch");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.bread;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "bread");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.paper;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "paper");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.bundle;
                }

                @Override
                public List<String> removes() {
                    return List.of(PREFIX + "bundle");
                }
            }
        );
    }
}
