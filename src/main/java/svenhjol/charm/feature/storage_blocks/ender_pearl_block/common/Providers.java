package svenhjol.charm.feature.storage_blocks.ender_pearl_block.common;

import svenhjol.charm.api.iface.ConditionalRecipe;
import svenhjol.charm.api.iface.ConditionalRecipeProvider;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlock;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<EnderPearlBlock> implements ConditionalRecipeProvider {
    public Providers(EnderPearlBlock feature) {
        super(feature);
    }

    @Override
    public List<ConditionalRecipe> getRecipeConditions() {
        return List.of(
            // Ender pearl block recipes.
            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().isEnabled();
                }

                @Override
                public List<String> recipes() {
                    return List.of("storage_blocks/ender_pearl*");
                }
            }
        );
    }
}
