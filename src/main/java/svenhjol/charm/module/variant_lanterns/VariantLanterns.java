package svenhjol.charm.module.variant_lanterns;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.block.CharmLanternBlock;
import svenhjol.charm.enums.IMetalMaterial;
import svenhjol.charm.enums.VanillaMetalMaterial;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedReturnValue")
@CommonModule(mod = Charm.MOD_ID, description = "Variants lanterns crafted from vanilla metal nuggets and torches.")
public class VariantLanterns extends CharmModule {
    public static Map<IMetalMaterial, List<CharmLanternBlock>> LANTERNS = new HashMap<>();

    @Override
    public void register() {
        for (IMetalMaterial material : VanillaMetalMaterial.getTypesWithout(VanillaMetalMaterial.IRON)) {
            if (material.hasNuggets()) {
                registerLantern(this, material, material.getSerializedName() + "_lantern");
                registerLantern(this, material, material.getSerializedName() + "_soul_lantern");
            }
        }
    }

    public static CharmLanternBlock registerLantern(CharmModule module, IMetalMaterial material, String name) {
        CharmLanternBlock lantern = new CharmLanternBlock(module, name, material);

        LANTERNS.computeIfAbsent(material, a -> new ArrayList<>());
        LANTERNS.get(material).add(lantern);

        return lantern;
    }
}
