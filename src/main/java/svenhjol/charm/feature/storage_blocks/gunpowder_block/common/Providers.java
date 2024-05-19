package svenhjol.charm.feature.storage_blocks.gunpowder_block.common;

import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.api.iface.IConditionalAdvancementProvider;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.GunpowderBlock;
import svenhjol.charm.foundation.feature.ProviderHolder;

import java.util.List;

public final class Providers extends ProviderHolder<GunpowderBlock> implements IConditionalRecipeProvider, IConditionalAdvancementProvider {
    public static final String RECIPE_NAME = "tnt_from_gunpowder_block";

    public Providers(GunpowderBlock feature) {
        super(feature);
    }

    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        var prefix = feature().snakeCaseName() + "/";

        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return GunpowderBlock.tntRecipe;
                }

                @Override
                public List<String> recipes() {
                    return List.of(prefix + RECIPE_NAME);
                }
            }
        );
    }

    @Override
    public List<IConditionalAdvancement> getAdvancementConditions() {
        var prefix = feature().snakeCaseName() + "/recipes/";

        return List.of(
            new IConditionalAdvancement() {
                @Override
                public boolean test() {
                    return GunpowderBlock.tntRecipe;
                }

                @Override
                public List<String> advancements() {
                    return List.of(prefix + RECIPE_NAME);
                }
            }
        );
    }
}
