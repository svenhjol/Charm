package svenhjol.charm.feature.wood.azalea_wood.common;

import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.wood.azalea_wood.AzaleaWood;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<AzaleaWood> implements IConditionalRecipeProvider {
    public Providers(AzaleaWood feature) {
        super(feature);
    }

    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return Resolve.isEnabled(Woodcutting.class);
                }

                @Override
                public List<String> recipes() {
                    return List.of("azalea_wood/woodcutting/*");
                }
            }
        );
    }
}
