package svenhjol.charm.feature.bat_buckets.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.bat_buckets.BatBucketsClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<BatBucketsClient> {
    public Registers(BatBucketsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common.registers.bucketItem,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.TADPOLE_BUCKET
        );
    }
}
