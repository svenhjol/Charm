package svenhjol.charm.block;

import net.minecraft.block.MapColor;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.*;
import svenhjol.charm.module.AzaleaWood;

public class AzaleaBlocks {
    public static class AzaleaButtonBlock extends CharmWoodenButtonBlock {
        public AzaleaButtonBlock(CharmModule module) {
            super(module, "azalea_button");
        }
    }

    public static class AzaleaDoorBlock extends CharmDoorBlock {
        public AzaleaDoorBlock(CharmModule module) {
            super(module, "azalea_door", AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaFenceBlock extends CharmFenceBlock {
        public AzaleaFenceBlock(CharmModule module) {
            super(module, "azalea_fence", AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaFenceGateBlock extends CharmFenceGateBlock {
        public AzaleaFenceGateBlock(CharmModule module) {
            super(module, "azalea_fence_gate", AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaLogBlock extends CharmLogBlock {
        public AzaleaLogBlock(CharmModule module) {
            super(module, "azalea_log", MapColor.BLACK, MapColor.STONE_GRAY);
        }
    }

    public static class StrippedAzaleaLogBlock extends CharmLogBlock {
        public StrippedAzaleaLogBlock(CharmModule module) {
            super(module, "stripped_azalea_log", MapColor.BLACK, MapColor.BLACK);
        }
    }

    public static class StrippedAzaleaWoodBlock extends CharmLogBlock {
        public StrippedAzaleaWoodBlock(CharmModule module) {
            super(module, "stripped_azalea_wood", MapColor.BLACK, MapColor.BLACK);
        }
    }

    public static class AzaleaPlanksBlock extends CharmPlanksBlock {
        public AzaleaPlanksBlock(CharmModule module) {
            super(module, "azalea_planks", MapColor.BLACK);
        }
    }

    public static class AzaleaPressurePlateBlock extends CharmPressurePlate {
        public AzaleaPressurePlateBlock(CharmModule module) {
            super(module, "azalea_pressure_plate", AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaSignBlock extends CharmSignBlock {
        public AzaleaSignBlock(CharmModule module) {
            super(module, "azalea_sign", AzaleaWood.SIGN_TYPE, MapColor.BLACK);
        }
    }

    public static class AzaleaSlabBlock extends CharmSlabBlock {
        public AzaleaSlabBlock(CharmModule module) {
            super(module, "azalea_slab", MapColor.BLACK);
        }
    }

    public static class AzaleaStairsBlock extends CharmStairsBlock {
        public AzaleaStairsBlock(CharmModule module) {
            super(module, "azalea_stairs", AzaleaWood.PLANKS);
        }
    }

    public static class AzaleaTrapdoorBlock extends CharmTrapdoorBlock {
        public AzaleaTrapdoorBlock(CharmModule module) {
            super(module, "azalea_trapdoor", MapColor.BLACK);
        }
    }

    public static class AzaleaWallSignBlock extends CharmWallSignBlock {
        public AzaleaWallSignBlock(CharmModule module) {
            super(module, "azalea_wall_sign", AzaleaWood.SIGN_BLOCK, AzaleaWood.SIGN_TYPE, MapColor.BLACK);
        }
    }

    public static class AzaleaWoodBlock extends CharmLogBlock {
        public AzaleaWoodBlock(CharmModule module) {
            super(module, "azalea_wood", MapColor.BROWN);
        }
    }
}
