package svenhjol.charm.feature.redstone_sand;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.common.CommonResolver;
import svenhjol.charm.feature.redstone_sand.client.Registers;

@Feature
public final class RedstoneSandClient extends ClientFeature implements CommonResolver<RedstoneSand> {
    public final Registers registers;

    public RedstoneSandClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<RedstoneSand> typeForCommon() {
        return RedstoneSand.class;
    }
}
