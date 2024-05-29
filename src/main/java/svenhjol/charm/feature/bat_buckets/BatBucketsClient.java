package svenhjol.charm.feature.bat_buckets;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.bat_buckets.client.Registers;

@Feature
public final class BatBucketsClient extends ClientFeature implements LinkedFeature<BatBuckets> {
    public final Registers registers;

    public BatBucketsClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<BatBuckets> typeForLinked() {
        return BatBuckets.class;
    }
}
