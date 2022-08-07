package svenhjol.charm.module.ebony_wood;

import net.minecraft.core.Holder;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.block.*;
import svenhjol.charm.loader.CharmModule;

import java.util.Random;

public class EbonyBlocks {
    public static class EbonyButtonBlock extends CharmWoodenButtonBlock {
        public EbonyButtonBlock(CharmModule module) {
            super(module, "ebony_button");
        }
    }

    public static class EbonyDoorBlock extends CharmDoorBlock {
        public EbonyDoorBlock(CharmModule module) {
            super(module, "ebony_door", EbonyWood.PLANKS);
        }
    }

    public static class EbonyFenceBlock extends CharmFenceBlock {
        public EbonyFenceBlock(CharmModule module) {
            super(module, "ebony_fence", EbonyWood.PLANKS);
        }
    }

    public static class EbonyFenceGateBlock extends CharmFenceGateBlock {
        public EbonyFenceGateBlock(CharmModule module) {
            super(module, "ebony_fence_gate", EbonyWood.PLANKS);
        }
    }

    public static class EbonyLeavesBlock extends CharmLeavesBlock {
        public EbonyLeavesBlock(CharmModule module) {
            super(module, "ebony_leaves");
        }
    }

    public static class EbonyLogBlock extends CharmLogBlock {
        public EbonyLogBlock(CharmModule module) {
            super(module, "ebony_log", MaterialColor.COLOR_BLACK, MaterialColor.STONE);
        }
    }

    public static class StrippedEbonyLogBlock extends CharmLogBlock {
        public StrippedEbonyLogBlock(CharmModule module) {
            super(module, "stripped_ebony_log", MaterialColor.COLOR_BLACK, MaterialColor.COLOR_BLACK);
        }
    }

    public static class StrippedEbonyWoodBlock extends CharmLogBlock {
        public StrippedEbonyWoodBlock(CharmModule module) {
            super(module, "stripped_ebony_wood", MaterialColor.COLOR_BLACK, MaterialColor.COLOR_BLACK);
        }
    }

    public static class EbonyPlanksBlock extends CharmPlanksBlock {
        public EbonyPlanksBlock(CharmModule module) {
            super(module, "ebony_planks", MaterialColor.COLOR_BLACK);
        }
    }

    public static class EbonyPressurePlateBlock extends CharmPressurePlate {
        public EbonyPressurePlateBlock(CharmModule module) {
            super(module, "ebony_pressure_plate", EbonyWood.PLANKS);
        }
    }

    public static class EbonySaplingBlock extends CharmSaplingBlock {
        public EbonySaplingBlock(CharmModule module) {
            super(module, "ebony_sapling", new AbstractTreeGrower() {
                @Nullable
                @Override
                protected Holder<? extends ConfiguredFeature<?, ?>> getConfiguredFeature(Random random, boolean bees) {
                    return EbonyWood.TREE;
                }
            });
        }
    }

    public static class EbonySignBlock extends CharmSignBlock {
        public EbonySignBlock(CharmModule module) {
            super(module, "ebony_sign", EbonyWood.SIGN_TYPE, MaterialColor.COLOR_BLACK);
        }
    }

    public static class EbonySlabBlock extends CharmWoodenSlabBlock {
        public EbonySlabBlock(CharmModule module) {
            super(module, "ebony_slab", MaterialColor.COLOR_BLACK);
        }
    }

    public static class EbonyStairsBlock extends CharmWoodenStairBlock {
        public EbonyStairsBlock(CharmModule module) {
            super(module, "ebony_stairs", EbonyWood.PLANKS);
        }
    }

    public static class EbonyTrapdoorBlock extends CharmTrapdoorBlock {
        public EbonyTrapdoorBlock(CharmModule module) {
            super(module, "ebony_trapdoor", MaterialColor.COLOR_BLACK);
        }
    }

    public static class EbonyWallSignBlock extends CharmWallSignBlock {
        public EbonyWallSignBlock(CharmModule module) {
            super(module, "ebony_wall_sign", EbonyWood.SIGN_BLOCK, EbonyWood.SIGN_TYPE, MaterialColor.COLOR_BLACK);
        }
    }

    public static class EbonyWoodBlock extends CharmLogBlock {
        public EbonyWoodBlock(CharmModule module) {
            super(module, "ebony_wood", MaterialColor.COLOR_BROWN);
        }
    }
}
