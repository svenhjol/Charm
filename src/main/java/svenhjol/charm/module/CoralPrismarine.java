package svenhjol.charm.module;

import svenhjol.charm.block.DarkCoralPrismarineBlock;
import svenhjol.charm.block.CoralPrismarineBlock;
import svenhjol.charm.block.CoralPrismarineBricksBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.ICoralMaterial;
import svenhjol.meson.enums.VanillaLivingCoralMaterial;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(description = "Coral can be combined with prismarine to make colored variants.")
public class CoralPrismarine extends MesonModule {
    public static final Map<ICoralMaterial, CoralPrismarineBlock> CORAL_PRISMARINE = new HashMap<>();
    public static final Map<ICoralMaterial, CoralPrismarineBricksBlock> CORAL_PRISMARINE_BRICKS = new HashMap<>();
    public static final Map<ICoralMaterial, DarkCoralPrismarineBlock> DARK_CORAL_PRISMARINE = new HashMap<>();


    @Override
    public void register() {
        for (ICoralMaterial type : VanillaLivingCoralMaterial.getTypes()) {
            CORAL_PRISMARINE.put(type, new CoralPrismarineBlock(this, type));
            CORAL_PRISMARINE_BRICKS.put(type, new CoralPrismarineBricksBlock(this, type));
            DARK_CORAL_PRISMARINE.put(type, new DarkCoralPrismarineBlock(this, type));
        }
    }
}
