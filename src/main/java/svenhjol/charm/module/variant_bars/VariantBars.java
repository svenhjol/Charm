package svenhjol.charm.module.variant_bars;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.block.CharmBarsBlock;
import svenhjol.charm.init.CharmDecoration;
import svenhjol.charm.module.CharmModule;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, client = VariantBarsClient.class, description = "Variant bars crafted from vanilla metal ingots.")
public class VariantBars extends CharmModule {
    public static Map<String, CharmBarsBlock> BARS = new HashMap<>();

    @Override
    public void register() {
        for (String material : new String[]{CharmDecoration.COPPER_VARIANT, CharmDecoration.GOLD_VARIANT, CharmDecoration.NETHERITE_VARIANT}) {
            BARS.put(material, new CharmBarsBlock(this, material + "_bars"));
        }
    }
}
