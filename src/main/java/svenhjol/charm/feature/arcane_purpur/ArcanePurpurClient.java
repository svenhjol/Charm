package svenhjol.charm.feature.arcane_purpur;

import svenhjol.charm.feature.arcane_purpur.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public class ArcanePurpurClient extends ClientFeature implements CommonResolver<ArcanePurpur> {
    public final Registers registers;

    public ArcanePurpurClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<ArcanePurpur> typeForCommon() {
        return ArcanePurpur.class;
    }
}
