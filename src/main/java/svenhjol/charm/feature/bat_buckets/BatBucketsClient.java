package svenhjol.charm.feature.bat_buckets;

import svenhjol.charm.feature.bat_buckets.client.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature
public final class BatBucketsClient extends ClientFeature {
    public final BatBuckets common;
    public final Registers registers;

    public BatBucketsClient(ClientLoader loader) {
        super(loader);

        common = Resolve.feature(BatBuckets.class);
        registers = new Registers(this);
    }
}
