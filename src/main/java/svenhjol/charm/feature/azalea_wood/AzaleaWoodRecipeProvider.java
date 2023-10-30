package svenhjol.charm.feature.azalea_wood;

import svenhjol.charm.Charm;
import svenhjol.charm.feature.woodcutters.Woodcutters;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony_api.iface.IConditionalRecipe;
import svenhjol.charmony_api.iface.IConditionalRecipeProvider;

import java.util.List;

public class AzaleaWoodRecipeProvider implements IConditionalRecipeProvider {
    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return Mods.common(Charm.ID).loader().isEnabled(Woodcutters.class);
                }

                @Override
                public List<String> recipes() {
                    return List.of("azalea_wood/woodcutting/*");
                }
            }
        );
    }
}
