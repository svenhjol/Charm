package svenhjol.charm.feature.totem_of_preserving;

import svenhjol.charmony_api.iface.IRecipeFilter;
import svenhjol.charmony_api.iface.IRecipeRemoveProvider;

import java.util.List;

public class TotemRecipeRemoveProvider implements IRecipeRemoveProvider {
    @Override
    public List<IRecipeFilter> getRecipeFilters() {
        return List.of(new IRecipeFilter() {
            @Override
            public boolean test() {
                return TotemOfPreserving.graveMode;
            }

            @Override
            public List<String> removes() {
                return List.of(
                    "charm:totem_of_preserving/*"
                );
            }
        });
    }
}
