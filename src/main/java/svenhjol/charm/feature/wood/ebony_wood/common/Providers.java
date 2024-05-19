package svenhjol.charm.feature.wood.ebony_wood.common;

import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.wood.ebony_wood.EbonyWood;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<EbonyWood> implements IConditionalRecipeProvider, IConditionalAdvancementProvider {
    public Providers(EbonyWood feature) {
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
                    return List.of("ebony_wood/woodcutting/*");
                }
            }
        );
    }

    @Override
    public List<IConditionalAdvancement> getAdvancementConditions() {
        return List.of(
            new IConditionalAdvancement() {
                @Override
                public boolean test() {
                    return Resolve.isEnabled(Woodcutting.class);
                }

                @Override
                public List<String> advancements() {
                    return List.of("ebony_wood/recipes/woodcutting/*");
                }
            }
        );
    }
}
