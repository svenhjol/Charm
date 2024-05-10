package svenhjol.charm;

import svenhjol.charm.feature.atlases.AtlasesClient;
import svenhjol.charm.feature.azalea_wood.AzaleaWoodClient;
import svenhjol.charm.feature.custom_wood.CustomWoodClient;
import svenhjol.charm.feature.variant_wood.VariantWoodClient;
import svenhjol.charm.feature.woodcutters.WoodcuttersClient;
import svenhjol.charm.foundation.client.ClientFeature;

import java.util.List;

public class CharmClient {
    public static List<Class<? extends ClientFeature>> features() {
        return List.of(
            AtlasesClient.class,
            AzaleaWoodClient.class,
//            BatBucketsClient.class,
//            ChairsClient.class,
//            ClearItemFramesClient.class,
//            ColoredGlintsClient.class,
//            ColoredGlintSmithingTemplatesClient.class,
//            CopperPistonsClient.class,
//            CoralSquidsClient.class,
            CustomWoodClient.class,
//            ExtractableEnchantmentsClient.class,
//            ExtraTooltips.class,
//            HoverSortingClient.class,
//            InventoryTidyingClient.class,
//            KilnsClient.class,
//            RecipesClient.class,
//            SmoothGlowstoneClient.class,
//            TotemOfPreservingClient.class,
//            VariantMobTextures.class,
            VariantWoodClient.class,
            WoodcuttersClient.class
        );
    }
}
