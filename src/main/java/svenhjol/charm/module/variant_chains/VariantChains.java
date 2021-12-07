package svenhjol.charm.module.variant_chains;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.block.CharmChainBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
@CommonModule(mod = Charm.MOD_ID, description = "Variant chains crafted from vanilla metal ingots and nuggets.")
public class VariantChains extends CharmModule {
    public static Map<IMetalMaterial, List<CharmChainBlock>> CHAINS = new HashMap<>();

    @Override
    public void register() {
        for (IMetalMaterial material : VanillaMetalMaterial.getTypesWithout(VanillaMetalMaterial.IRON)) {
            if (material.hasNuggets()) {
                registerChain(this, material, material.getSerializedName() + "_chain");
            }
        }
    }

    public static CharmChainBlock registerChain(CharmModule module, IMetalMaterial material, String name) {
        CharmChainBlock chain = new CharmChainBlock(module, name, material);

        CHAINS.computeIfAbsent(material, a -> new ArrayList<>());
        CHAINS.get(material).add(chain);

        return chain;
    }
}
