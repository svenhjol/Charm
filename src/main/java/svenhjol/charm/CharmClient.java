package svenhjol.charm;

import svenhjol.charm.feature.colored_glints.ColoredGlintsClient;
import svenhjol.charm.feature.custom_wood.CustomWoodClient;
import svenhjol.charm.feature.recipes.RecipesClient;
import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstoneClient;
import svenhjol.charm.feature.variant_wood.VariantWoodClient;
import svenhjol.charm.foundation.client.ClientFeature;

import java.util.List;

public class CharmClient {
    public static List<Class<? extends ClientFeature>> features() {
        return List.of(
            ColoredGlintsClient.class,
            CustomWoodClient.class,
            RecipesClient.class,
            SmoothGlowstoneClient.class,
            VariantWoodClient.class
        );
    }
}
