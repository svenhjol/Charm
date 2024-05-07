package svenhjol.charm.feature.totem_of_preserving;

import svenhjol.charm.feature.totem_of_preserving.client.Registers;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

public class TotemOfPreservingClient extends ClientFeature {
    public static Registers registers;

    @Override
    public Class<? extends CommonFeature> relatedCommonFeature() {
        return TotemOfPreserving.class;
    }

    @Override
    public void setup() {
        registers = new Registers(this);
    }
}
