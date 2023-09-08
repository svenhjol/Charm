package svenhjol.charm.feature.storage_blocks;

import svenhjol.charmony.api.iface.IRecipeFilter;
import svenhjol.charmony.api.iface.IRecipeRemoveProvider;

import java.util.List;

public class StorageBlockRecipeFilters implements IRecipeRemoveProvider {
    @Override
    public List<IRecipeFilter> getRecipeFilters() {
        return List.of(
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !StorageBlocks.gunpowder;
                }

                @Override
                public List<String> removes() {
                    return List.of("storage_blocks/gunpowder*");
                }
            }
        );
    }
}
