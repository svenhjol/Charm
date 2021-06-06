package svenhjol.charm.module.azalea_wood;

import svenhjol.charm.module.CharmModule;
import net.minecraft.world.level.material.MaterialColor;
import svenhjol.charm.block.*;
import svenhjol.charm.module.azalea_wood.AzaleaWood;

public class AzaleaBlocks {
    public static class AzaleaButtonBlock extends CharmWoodenButtonBlock {
        public AzaleaButtonBlock(CharmModule module) {
            super(module, "azalea_button");
        }
    }

    public static class AzaleaDoorBlock extends CharmDoorBlock {
        public AzaleaDoorBlock(CharmModule module) {
            super(module, "azalea_door", svenhjol.charm.module.azalea_wood.AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaFenceBlock extends CharmFenceBlock {
        public AzaleaFenceBlock(CharmModule module) {
            super(module, "azalea_fence", svenhjol.charm.module.azalea_wood.AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaFenceGateBlock extends CharmFenceGateBlock {
        public AzaleaFenceGateBlock(CharmModule module) {
            super(module, "azalea_fence_gate", svenhjol.charm.module.azalea_wood.AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaLogBlock extends CharmLogBlock {
        public AzaleaLogBlock(CharmModule module) {
            super(module, "azalea_log", MaterialColor.COLOR_BLACK, MaterialColor.STONE);
        }
    }

    public static class StrippedAzaleaLogBlock extends CharmLogBlock {
        public StrippedAzaleaLogBlock(CharmModule module) {
            super(module, "stripped_azalea_log", MaterialColor.COLOR_BLACK, MaterialColor.COLOR_BLACK);
        }
    }

    public static class StrippedAzaleaWoodBlock extends CharmLogBlock {
        public StrippedAzaleaWoodBlock(CharmModule module) {
            super(module, "stripped_azalea_wood", MaterialColor.COLOR_BLACK, MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaPlanksBlock extends CharmPlanksBlock {
        public AzaleaPlanksBlock(CharmModule module) {
            super(module, "azalea_planks", MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaPressurePlateBlock extends CharmPressurePlate {
        public AzaleaPressurePlateBlock(CharmModule module) {
            super(module, "azalea_pressure_plate", svenhjol.charm.module.azalea_wood.AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaSignBlock extends CharmSignBlock {
        public AzaleaSignBlock(CharmModule module) {
            super(module, "azalea_sign", svenhjol.charm.module.azalea_wood.AzaleaWood.SIGN_TYPE, MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaSlabBlock extends CharmSlabBlock {
        public AzaleaSlabBlock(CharmModule module) {
            super(module, "azalea_slab", MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaStairsBlock extends CharmStairsBlock {
        public AzaleaStairsBlock(CharmModule module) {
            super(module, "azalea_stairs", svenhjol.charm.module.azalea_wood.AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaTrapdoorBlock extends CharmTrapdoorBlock {
        public AzaleaTrapdoorBlock(CharmModule module) {
            super(module, "azalea_trapdoor", MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaWallSignBlock extends CharmWallSignBlock {
        public AzaleaWallSignBlock(CharmModule module) {
            super(module, "azalea_wall_sign", svenhjol.charm.module.azalea_wood.AzaleaWood.SIGN_BLOCK, AzaleaWood.SIGN_TYPE, MaterialColor.COLOR_BLACK);
        }
    }

    public static class AzaleaWoodBlock extends CharmLogBlock {
        public AzaleaWoodBlock(CharmModule module) {
            super(module, "azalea_wood", MaterialColor.COLOR_BROWN);
        }
    }
}
