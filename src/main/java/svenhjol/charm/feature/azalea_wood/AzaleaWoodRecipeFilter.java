package svenhjol.charm.feature.azalea_wood;

import svenhjol.charmony.Charmony;
import svenhjol.charmapi.iface.IRecipeFilter;
import svenhjol.charmapi.iface.IRecipeRemoveProvider;
import svenhjol.charmony.feature.woodcutting.Woodcutting;

import java.util.List;

public class AzaleaWoodRecipeFilter implements IRecipeRemoveProvider {
    @Override
    public List<IRecipeFilter> getRecipeFilters() {
        return List.of(
            new IRecipeFilter() {
                @Override
                public boolean test() {
                    return !Charmony.instance().loader().isEnabled(Woodcutting.class);
                }

                @Override
                public List<String> removes() {
                    return List.of("azalea_wood/woodcutting/*");
                }
            }
        );
    }
}
