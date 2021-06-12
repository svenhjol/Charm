package svenhjol.charm.module.variant_bars;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.block.CharmBarsBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.module.CharmModule;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, client = VariantBarsClient.class, description = "Variant bars crafted from vanilla metal ingots.")
public class VariantBars extends CharmModule {
    public static Map<IMetalMaterial, CharmBarsBlock> BARS = new HashMap<>();

    @Override
    public void register() {
        for (IMetalMaterial material : VanillaMetalMaterial.getTypes()) {
            BARS.put(material, new CharmBarsBlock(this, material.getSerializedName() + "_bars"));
        }
    }
}
