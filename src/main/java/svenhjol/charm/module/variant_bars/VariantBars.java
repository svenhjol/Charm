package svenhjol.charm.module.variant_bars;

import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.block.CharmBarsBlock;
import svenhjol.charm.annotation.Module;

import java.util.ArrayList;
import java.util.List;

@Module(mod = Charm.MOD_ID, client = VariantBarsClient.class, description = "Variant bars crafted from vanilla metal ingots.")
public class VariantBars extends CharmModule {
    public static List<CharmBarsBlock> BARS = new ArrayList<>();

    @Override
    public void register() {
        for (String material : new String[]{"copper", "gold", "netherite"}) {
            BARS.add(new CharmBarsBlock(this, material + "_bars"));
        }
    }
}
