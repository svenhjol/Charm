package svenhjol.charm.feature.storage_blocks;

import svenhjol.charmony.api.iface.IRecipeFilter;
import svenhjol.charmony.api.iface.IRecipeRemoveProvider;

import java.util.List;

public class StorageBlockRecipeFilters implements IRecipeRemoveProvider {
    @Override
    public List<IRecipeFilter> getRecipeFilters() {
        return List.of(
            // Remove ender pearl block recipes.
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !StorageBlocks.enderPearlsEnabled;
                }

                @Override
                public List<String> removes() {
                    return List.of("storage_blocks/ender_pearl*");
                }
            },

            // Remove gunpowder block recipes.
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !StorageBlocks.gunpowderEnabled;
                }

                @Override
                public List<String> removes() {
                    return List.of("storage_blocks/gunpowder*");
                }
            },

            // Remove sugar block recipes.
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !StorageBlocks.sugarEnabled;
                }

                @Override
                public List<String> removes() {
                    return List.of("storage_blocks/sugar*");
                }
            }
        );
    }
}
