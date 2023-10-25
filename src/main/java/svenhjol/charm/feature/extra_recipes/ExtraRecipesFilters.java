package svenhjol.charm.feature.extra_recipes;

import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.common.CommonLoader;
import svenhjol.charmony_api.iface.IAdvancementFilter;
import svenhjol.charmony_api.iface.IAdvancementRemoveProvider;
import svenhjol.charmony_api.iface.IRecipeFilter;
import svenhjol.charmony_api.iface.IRecipeRemoveProvider;

import java.util.ArrayList;
import java.util.List;

public class ExtraRecipesFilters implements IRecipeRemoveProvider, IAdvancementRemoveProvider {
    static final String RECIPE_PREFIX = "extra_recipes/";
    static final String ADVANCEMENT_PREFIX = "extra_recipes/recipes/";

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
                    return List.of(RECIPE_PREFIX + "*from_blasting_raw_*_block");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.gildedBlackstone;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "gilded_blackstone");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.snowballs;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "snowballs_from_snow_block");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.quartz;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "quartz_from_quartz_block");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.clay;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "clay_balls_from_clay_block");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.cyanDye;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "cyan_dye");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.greenDye;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "green_dye");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.soulTorch;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "soul_torch");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.bread;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "bread");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.paper;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "paper");
                }
            },

            new IRecipeFilter() {
                @Override
                public boolean test() {
                    var gunpowderEnabled = loader().get(StorageBlocks.class)
                        .map(StorageBlocks::isGunpowderEnabled).orElse(false);
                    return !ExtraRecipes.tntFromGunpowderBlock || !gunpowderEnabled;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "tnt_from_gunpowder_block");
                }
            },
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !ExtraRecipes.bundle;
                }

                @Override
                public List<String> removes() {
                    return List.of(RECIPE_PREFIX + "bundle");
                }
            }
        );
    }

    /**
     * The advancement filters are basically the same as recipe filters just with a different prefix.
     */
    @Override
    public List<IAdvancementFilter> getAdvancementFilters() {
        List<IAdvancementFilter> advancementFilters = new ArrayList<>();
        var recipeFilters = getRecipeFilters();

        for (var recipeFilter : recipeFilters) {
            advancementFilters.add(new IAdvancementFilter() {
                @Override
                public boolean test() {
                    return recipeFilter.test();
                }

                @Override
                public List<String> removes() {
                    var list = recipeFilter.removes();
                    return list.stream().map(i -> i.replace(RECIPE_PREFIX, ADVANCEMENT_PREFIX)).toList();
                }
            });
        }

        return advancementFilters;
    }

    /**
     * Helper to get the current charm loader.
     */
    private CommonLoader loader() {
        return Mods.common(Charm.ID).loader();
    }
}
