package svenhjol.charm.feature.azalea_wood.common;

import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.foundation.Resolve;

import java.util.List;

public class DataProviders implements IConditionalRecipeProvider {
    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return Resolve.common(Charm.ID).isEnabled(Woodcutting.class);
                }

                @Override
                public List<String> recipes() {
                    return List.of("azalea_wood/woodcutting/*");
                }
            }
        );
    }
}
