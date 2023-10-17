package svenhjol.charm.feature.totem_of_preserving;

import svenhjol.charmony_api.iface.IAdvancementFilter;
import svenhjol.charmony_api.iface.IAdvancementRemoveProvider;
import svenhjol.charmony_api.iface.IRecipeFilter;
import svenhjol.charmony_api.iface.IRecipeRemoveProvider;

import java.util.List;

public class TotemDataRemoveProvider implements IRecipeRemoveProvider, IAdvancementRemoveProvider {
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

    @Override
    public List<IAdvancementFilter> getAdvancementFilters() {
        return List.of(new IAdvancementFilter() {
            @Override
            public boolean test() {
                return TotemOfPreserving.graveMode;
            }

            @Override
            public List<String> removes() {
                return List.of(
                    "charm:totem_of_preserving/recipes/*"
                );
            }
        });
    }
}
