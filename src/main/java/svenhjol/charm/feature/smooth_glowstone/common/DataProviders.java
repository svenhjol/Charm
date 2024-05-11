package svenhjol.charm.feature.smooth_glowstone.common;

import svenhjol.charm.Charm;
import svenhjol.charm.api.iface.IConditionalRecipe;
import svenhjol.charm.api.iface.IConditionalRecipeProvider;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstone;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class DataProviders extends FeatureHolder<SmoothGlowstone> implements IConditionalRecipeProvider {
    public DataProviders(SmoothGlowstone feature) {
        super(feature);
    }

    @Override
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(
            new IConditionalRecipe() {
                @Override
                public boolean test() {
                    return Resolve.featureEnabled(Side.COMMON, Charm.id("firing"));
                }

                @Override
                public List<String> recipes() {
                    return List.of(
                        "smooth_glowstone/firing/*"
                    );
                }
            }
        );
    }
}
