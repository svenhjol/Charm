package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.block.CoralSeaLanternBlock;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.enums.ICoralMaterial;
import svenhjol.charm.base.enums.VanillaLivingCoralMaterial;
import svenhjol.charm.base.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, description = "Coral can be combined with sea lanterns to make colored variants.")
public class CoralSeaLanterns extends CharmModule {
    public static final Map<ICoralMaterial, CoralSeaLanternBlock> CORAL_SEA_LANTERNS = new HashMap<>();

    @Override
    public void register() {
        for (ICoralMaterial type : VanillaLivingCoralMaterial.getTypes()) {
            CORAL_SEA_LANTERNS.put(type, new CoralSeaLanternBlock(this, type));
        }
    }
}
