package svenhjol.charm.feature.storage_blocks.gunpowder_block.common;

import svenhjol.charm.api.iface.ConditionalAdvancement;
import svenhjol.charm.api.iface.ConditionalAdvancementProvider;
import svenhjol.charm.api.iface.ConditionalRecipe;
import svenhjol.charm.api.iface.ConditionalRecipeProvider;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.GunpowderBlock;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<GunpowderBlock> implements ConditionalRecipeProvider, ConditionalAdvancementProvider {
    public static final String RECIPE_NAME = "tnt_from_gunpowder_block";

    public Providers(GunpowderBlock feature) {
        super(feature);
    }

    @Override
    public List<ConditionalRecipe> getRecipeConditions() {
        var prefix = feature().snakeCaseName() + "/";

        return List.of(
            new ConditionalRecipe() {
                @Override
                public boolean test() {
                    return feature().tntRecipe();
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + RECIPE_NAME);
                }
            }
        );
    }

    @Override
    public List<ConditionalAdvancement> getAdvancementConditions() {
        var prefix = feature().snakeCaseName() + "/recipes/";

        return List.of(
            new ConditionalAdvancement() {
                @Override
                public boolean test() {
                    return feature().tntRecipe();
                }

                @Override
                public List<String> advancements() {
                    return List.of(prefix + RECIPE_NAME);
                }
            }
        );
    }
}
