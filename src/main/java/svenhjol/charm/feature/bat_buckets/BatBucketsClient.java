package svenhjol.charm.feature.bat_buckets;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.Register;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;

public class BatBucketsClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonClass() {
        return BatBuckets.class;
    }

    @Override
    public Optional<Register<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }
}
