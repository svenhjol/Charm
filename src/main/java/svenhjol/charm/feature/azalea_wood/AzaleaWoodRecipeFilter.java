package svenhjol.charm.feature.azalea_wood;

import svenhjol.charm.Charm;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony_api.iface.IRecipeFilter;
import svenhjol.charmony_api.iface.IRecipeRemoveProvider;

import java.util.List;

public class AzaleaWoodRecipeFilter implements IRecipeRemoveProvider {
    @Override
    public List<IRecipeFilter> getRecipeFilters() {
        return List.of(
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !Mods.common(Charm.ID).loader().isEnabled(Woodcutters.class);
                }

                @Override
                public List<String> removes() {
                    return List.of("azalea_wood/woodcutting/*");
                }
            }
        );
    }
}
