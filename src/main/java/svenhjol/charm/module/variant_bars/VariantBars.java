package svenhjol.charm.module.variant_bars;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.block.CharmBarsBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
@CommonModule(mod = Charm.MOD_ID, description = "Variant bars crafted from vanilla metal ingots.")
public class VariantBars extends CharmModule {
    public static Map<IMetalMaterial, List<CharmBarsBlock>> BARS = new HashMap<>();

    @Override
    public void register() {
        for (IMetalMaterial material : VanillaMetalMaterial.getTypesWithout(VanillaMetalMaterial.IRON)) {
            registerBars(this, material, material.getSerializedName() + "_bars");
        }
    }

    public static CharmBarsBlock registerBars(CharmModule module, IMetalMaterial material, String name) {
        CharmBarsBlock bars = new CharmBarsBlock(module, name, material);
        BARS.computeIfAbsent(material, a -> new ArrayList<>());
        BARS.get(material).add(bars);
        return bars;
    }
}
