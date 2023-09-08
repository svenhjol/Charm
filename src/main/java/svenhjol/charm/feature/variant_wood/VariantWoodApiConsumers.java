package svenhjol.charm.feature.variant_wood;

import svenhjol.charm.api.IVariantBookshelfProvider;
import svenhjol.charm.api.IVariantChestBoatProvider;
import svenhjol.charm.api.IVariantChiseledBookshelfProvider;
import svenhjol.charm.api.IVariantLadderProvider;
import svenhjol.charmony.api.iface.IVariantBarrelProvider;
import svenhjol.charmony.api.iface.IVariantChestProvider;
import svenhjol.charmony.helper.ApiHelper;

public class VariantWoodApiConsumers {
    public VariantWoodApiConsumers(VariantWood feature) {
        ApiHelper.consume(IVariantBarrelProvider.class,
            provider -> provider.getVariantBarrels().forEach(feature::registerBarrel));

        ApiHelper.consume(IVariantBookshelfProvider.class,
            provider -> provider.getVariantBookshelves().forEach(feature::registerBookshelf));

        ApiHelper.consume(IVariantChestProvider.class,
            provider -> provider.getVariantChests().forEach(chest -> {
                feature.registerChest(chest);
                feature.registerTrappedChest(chest);
            }));

        ApiHelper.consume(IVariantChestBoatProvider.class,
            provider -> provider.getVariantChestBoatDefinitions().forEach(feature::registerChestBoat));

        ApiHelper.consume(IVariantChiseledBookshelfProvider.class,
            provider -> provider.getVariantChiseledBookshelves().forEach(feature::registerChiseledBookshelf));

        ApiHelper.consume(IVariantLadderProvider.class,
            provider -> provider.getVariantLadders().forEach(feature::registerLadder));
    }
}
