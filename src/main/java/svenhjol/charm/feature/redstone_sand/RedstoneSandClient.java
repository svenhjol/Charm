package svenhjol.charm.feature.redstone_sand;

import svenhjol.charm.feature.redstone_sand.client.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonResolver;

@Feature
public class RedstoneSandClient extends ClientFeature implements CommonResolver<RedstoneSand> {
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
