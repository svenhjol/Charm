package svenhjol.charm.feature.bat_buckets.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.bat_buckets.BatBucketsClient;

public final class Registers extends RegisterHolder<BatBucketsClient> {
    public Registers(BatBucketsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().linked().registers.bucketItem.get(),
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.TADPOLE_BUCKET
        );
    }
}
