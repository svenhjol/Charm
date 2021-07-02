package svenhjol.charm.module.coral_sea_lanterns;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.enums.ICoralMaterial;
import svenhjol.charm.enums.VanillaLivingCoralMaterial;
import svenhjol.charm.loader.CharmModule;

import java.util.HashMap;
import java.util.Map;

@CommonModule(mod = Charm.MOD_ID, description = "Coral can be combined with sea lanterns to make colored variants.")
public class CoralSeaLanterns extends CharmModule {
    public static final Map<ICoralMaterial, CoralSeaLanternBlock> CORAL_SEA_LANTERNS = new HashMap<>();

    @Override
    public void register() {
        for (ICoralMaterial type : VanillaLivingCoralMaterial.getTypes()) {
            CORAL_SEA_LANTERNS.put(type, new CoralSeaLanternBlock(this, type));
        }
    }
}
