package svenhjol.charm.feature.storage_blocks;

import svenhjol.charmony_api.iface.IConditionalRecipe;
import svenhjol.charmony_api.iface.IConditionalRecipeProvider;

import java.util.List;

public class StorageBlockDataProviders implements IConditionalRecipeProvider {
    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(
            // Ender pearl block recipes.
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return StorageBlocks.enderPearlsEnabled;
                }

                @Override
                public List<String> recipes() {
                    return List.of("storage_blocks/ender_pearl*");
                }
            },

            // Gunpowder block recipes.
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return StorageBlocks.gunpowderEnabled;
                }

                @Override
                public List<String> recipes() {
                    return List.of("storage_blocks/gunpowder*");
                }
            },

            // Sugar block recipes.
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return StorageBlocks.sugarEnabled;
                }

                @Override
                public List<String> recipes() {
                    return List.of("storage_blocks/sugar*");
                }
            }
        );
    }
}
