package svenhjol.charm.feature.bat_buckets;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class BatBucketsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(BatBuckets.class));
    }

    @Override
    public void register() {
        if (isEnabled()) {
            CharmClient.instance().registry().itemTab(
                BatBuckets.BAT_BUCKET_ITEM,
                CreativeModeTabs.TOOLS_AND_UTILITIES,
                Items.TADPOLE_BUCKET
            );
        }
    }
}
