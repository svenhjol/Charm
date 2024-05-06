package svenhjol.charm.feature.bat_buckets.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.bat_buckets.BatBuckets;
import svenhjol.charm.feature.bat_buckets.BatBucketsClient;
import svenhjol.charm.foundation.feature.Register;

public final class Registers extends Register<BatBucketsClient> {
    public Registers(BatBucketsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature.registry().itemTab(
            BatBuckets.registers.bucketItem,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.TADPOLE_BUCKET
        );
    }
}
