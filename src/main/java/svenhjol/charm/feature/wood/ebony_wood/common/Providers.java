package svenhjol.charm.feature.wood.ebony_wood.common;

import svenhjol.charm.api.iface.ConditionalAdvancement;
import svenhjol.charm.api.iface.ConditionalAdvancementProvider;
import svenhjol.charm.api.iface.ConditionalRecipe;
import svenhjol.charm.api.iface.ConditionalRecipeProvider;
import svenhjol.charm.feature.wood.ebony_wood.EbonyWood;
import svenhjol.charm.feature.woodcutting.Woodcutting;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<EbonyWood> implements ConditionalRecipeProvider, ConditionalAdvancementProvider {
    public Providers(EbonyWood feature) {
        super(feature);
    }

    @Override
    public List<ConditionalRecipe> getRecipeConditions() {
        return List.of(
            new ConditionalRecipe() {
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
    public List<ConditionalAdvancement> getAdvancementConditions() {
        return List.of(
            new ConditionalAdvancement() {
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
