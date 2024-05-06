package svenhjol.charm.feature.bat_buckets;

import svenhjol.charm.feature.bat_buckets.client.Registers;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

public class BatBucketsClient extends ClientFeature {
    public static Registers registers;

    @Override
    public Class<? extends CommonFeature> relatedCommonFeature() {
        return BatBuckets.class;
    }

    @Override
    public void setup() {
        registers = new Registers(this);
    }
}
