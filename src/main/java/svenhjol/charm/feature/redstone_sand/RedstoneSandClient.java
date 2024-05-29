package svenhjol.charm.feature.redstone_sand;

import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.client.ClientFeature;
import svenhjol.charm.charmony.client.ClientLoader;
import svenhjol.charm.charmony.feature.LinkedFeature;
import svenhjol.charm.feature.redstone_sand.client.Registers;

@Feature
public final class RedstoneSandClient extends ClientFeature implements LinkedFeature<RedstoneSand> {
    public final Registers registers;

    public RedstoneSandClient(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    @Override
    public Class<RedstoneSand> typeForLinked() {
        return RedstoneSand.class;
    }
}
