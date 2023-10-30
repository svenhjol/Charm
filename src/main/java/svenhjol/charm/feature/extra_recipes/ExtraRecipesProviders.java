package svenhjol.charm.feature.extra_recipes;

import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.common.CommonLoader;
import svenhjol.charmony_api.iface.IConditionalAdvancement;
import svenhjol.charmony_api.iface.IConditionalAdvancementProvider;
import svenhjol.charmony_api.iface.IConditionalRecipe;
import svenhjol.charmony_api.iface.IConditionalRecipeProvider;

import java.util.ArrayList;
import java.util.List;

public class ExtraRecipesProviders implements IConditionalRecipeProvider, IConditionalAdvancementProvider {
    static final String RECIPE_PREFIX = "extra_recipes/";
    static final String ADVANCEMENT_PREFIX = "extra_recipes/recipes/";

    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.rawOreBlocks;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "*from_blasting_raw_*_block");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.gildedBlackstone;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "gilded_blackstone");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.snowballs;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "snowballs_from_snow_block");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.quartz;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "quartz_from_quartz_block");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.clay;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "clay_balls_from_clay_block");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.cyanDye;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "cyan_dye");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.greenDye;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "green_dye");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.soulTorch;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "soul_torch");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.bread;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "bread");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.paper;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "paper");
                }
            },

            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    var gunpowderEnabled = loader().get(StorageBlocks.class)
                        .map(StorageBlocks::isGunpowderEnabled).orElse(false);
                    return ExtraRecipes.tntFromGunpowderBlock && gunpowderEnabled;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "tnt_from_gunpowder_block");
                }
            },
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return ExtraRecipes.bundle;
                }

                @Override
                public List<String> recipes() {
                    return List.of(RECIPE_PREFIX + "bundle");
                }
            }
        );
    }

    /**
     * The advancement conditions are basically the same as recipe conditions just with a different prefix.
     */
    @Override
    public List<IConditionalAdvancement> getAdvancementConditions() {
        List<IConditionalAdvancement> advancements = new ArrayList<>();
        var recipes = getRecipeConditions();

        for (var recipe : recipes) {
            advancements.add(new IConditionalAdvancement() {
                @Override
                public boolean test() {
                    return recipe.test();
                }

                @Override
                public List<String> advancements() {
                    var list = recipe.recipes();
                    return list.stream().map(i -> i.replace(RECIPE_PREFIX, ADVANCEMENT_PREFIX)).toList();
                }
            });
        }

        return advancements;
    }

    /**
     * Helper to get the current charm loader.
     */
    private CommonLoader loader() {
        return Mods.common(Charm.ID).loader();
    }
}
