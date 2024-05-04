package svenhjol.charm.feature.bat_buckets;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.foundation.Registration;

public final class ClientRegistration extends Registration<BatBucketsClient>  {
    public ClientRegistration(BatBucketsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature.registry().itemTab(
            BatBuckets.bucketItem,
            CreativeModeTabs.TOOLS_AND_UTILITIES,
            Items.TADPOLE_BUCKET
        );
    }
}
