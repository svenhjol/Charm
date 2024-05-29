package svenhjol.charm.feature.arcane_purpur;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.feature.arcane_purpur.client.Registers;

@Feature
public final class ArcanePurpurClient extends ClientFeature implements CommonResolver<ArcanePurpur> {
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
