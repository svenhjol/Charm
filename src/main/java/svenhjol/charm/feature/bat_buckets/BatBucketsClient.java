package svenhjol.charm.feature.bat_buckets;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class BatBucketsClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return BatBuckets.class;
    }

    @Override
    public void register() {
        if (isEnabled()) {
            mod().registry().itemTab(
                BatBuckets.bucketItem,
                CreativeModeTabs.TOOLS_AND_UTILITIES,
                Items.TADPOLE_BUCKET
            );
        }
    }
}
