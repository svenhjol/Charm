package svenhjol.charm.module.azalea_wood;

import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm.block.*;
import svenhjol.charm.loader.CharmCommonModule;

import static svenhjol.charm.module.azalea_wood.AzaleaWood.*;

public class AzaleaBlocks {
    public static class AzaleaButtonBlock extends CharmWoodenButtonBlock {
        public AzaleaButtonBlock(CharmCommonModule module) {
            super(module, "azalea_button");
        }
    }

    public static class AzaleaDoorBlock extends CharmDoorBlock {
        public AzaleaDoorBlock(CharmCommonModule module) {
            super(module, "azalea_door", PLANKS);
        }
    }

    public static class AzaleaFenceBlock extends CharmFenceBlock {
        public AzaleaFenceBlock(CharmCommonModule module) {
            super(module, "azalea_fence", PLANKS);
        }
    }

    public static class AzaleaFenceGateBlock extends CharmFenceGateBlock {
        public AzaleaFenceGateBlock(CharmCommonModule module) {
            super(module, "azalea_fence_gate", PLANKS);
        }
    }

    public static class AzaleaLogBlock extends CharmLogBlock {
        public AzaleaLogBlock(CharmCommonModule module) {
            super(module, "azalea_log", MaterialColor.COLOR_BLACK, MaterialColor.STONE);
        }
    }

    public static class StrippedAzaleaLogBlock extends CharmLogBlock {
        public StrippedAzaleaLogBlock(CharmCommonModule module) {
            super(module, "stripped_azalea_log", MaterialColor.COLOR_BLACK, MaterialColor.COLOR_BLACK);
        }
    }

    public static class StrippedAzaleaWoodBlock extends CharmLogBlock {
        public StrippedAzaleaWoodBlock(CharmCommonModule module) {
            super(module, "stripped_azalea_wood", MaterialColor.COLOR_BLACK, MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaPlanksBlock extends CharmPlanksBlock {
        public AzaleaPlanksBlock(CharmCommonModule module) {
            super(module, "azalea_planks", MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaPressurePlateBlock extends CharmPressurePlate {
        public AzaleaPressurePlateBlock(CharmCommonModule module) {
            super(module, "azalea_pressure_plate", PLANKS);
        }
    }

    public static class AzaleaSignBlock extends CharmSignBlock {
        public AzaleaSignBlock(CharmCommonModule module) {
            super(module, "azalea_sign", SIGN_TYPE, MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaSlabBlock extends CharmSlabBlock {
        public AzaleaSlabBlock(CharmCommonModule module) {
            super(module, "azalea_slab", MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaStairsBlock extends CharmStairsBlock {
        public AzaleaStairsBlock(CharmCommonModule module) {
            super(module, "azalea_stairs", PLANKS);
        }
    }

    public static class AzaleaTrapdoorBlock extends CharmTrapdoorBlock {
        public AzaleaTrapdoorBlock(CharmCommonModule module) {
            super(module, "azalea_trapdoor", MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaWallSignBlock extends CharmWallSignBlock {
        public AzaleaWallSignBlock(CharmCommonModule module) {
            super(module, "azalea_wall_sign", SIGN_BLOCK, SIGN_TYPE, MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaWoodBlock extends CharmLogBlock {
        public AzaleaWoodBlock(CharmCommonModule module) {
            super(module, "azalea_wood", MaterialColor.COLOR_BROWN);
        }
    }
}
