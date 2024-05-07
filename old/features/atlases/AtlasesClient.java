package svenhjol.charm.feature.atlases;

import svenhjol.charm.feature.atlases.client.Handlers;
import svenhjol.charm.feature.atlases.client.Registers;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

public class AtlasesClient extends ClientFeature {
    public static Registers registers;
    public static Handlers handlers;

    @Override
    public Class<? extends CommonFeature> relatedCommonFeature() {
        return Atlases.class;
    }

    @Override
    public void setup() {
        handlers = new Handlers(this);
        registers = new Registers(this);
    }
}
