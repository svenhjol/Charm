package svenhjol.charm.feature.bat_buckets;

import svenhjol.charm.feature.bat_buckets.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public final class BatBucketsClient extends ClientFeature implements CommonResolver<BatBuckets> {
    public final Registers registers;

    public BatBucketsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<BatBuckets> typeForCommon() {
        return BatBuckets.class;
    }
}
