package svenhjol.charm.feature.extractable_enchantments;

import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

public class ExtractableEnchantmentsClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> relatedCommonFeature() {
        return ExtractableEnchantments.class;
    }
}
