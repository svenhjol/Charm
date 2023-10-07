package svenhjol.charm.feature.bat_buckets;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(mod = CharmClient.MOD_ID, feature = BatBuckets.class)
public class BatBucketsClient extends CharmonyFeature {
    @Override
    public void register() {
        if (isEnabled()) {
            CharmClient.instance().registry().itemTab(
                BatBuckets.bucketItem,
                CreativeModeTabs.TOOLS_AND_UTILITIES,
                Items.TADPOLE_BUCKET
            );
        }
    }
}
