package svenhjol.charm.feature.bat_buckets;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.feature.bat_buckets.client.Registers;

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
