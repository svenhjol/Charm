package svenhjol.charm.feature.storage_blocks.ender_pearl_block.common;

import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlock;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<EnderPearlBlock> implements IConditionalRecipeProvider {
    public Providers(EnderPearlBlock feature) {
        super(feature);
    }

    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(
            // Ender pearl block recipes.
            new IConditionalRecipe() {
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
