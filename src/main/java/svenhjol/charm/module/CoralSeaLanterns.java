package svenhjol.charm.module;

import svenhjol.charm.block.CoralSeaLanternBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.ICoralMaterial;
import svenhjol.meson.enums.VanillaLivingCoralMaterial;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(description = "Coral can be combined with sea lanterns to make colored variants.")
public class CoralSeaLanterns extends MesonModule {
    public static final Map<ICoralMaterial, CoralSeaLanternBlock> CORAL_SEA_LANTERNS = new HashMap<>();

    @Override
    public void register() {
        for (ICoralMaterial type : VanillaLivingCoralMaterial.getTypes()) {
            CORAL_SEA_LANTERNS.put(type, new CoralSeaLanternBlock(this, type));
        }
    }
}
