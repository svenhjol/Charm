package svenhjol.charm.block;

import net.minecraft.block.MapColor;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.block.*;
import svenhjol.charm.module.EbonyWood;

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
            super(module, "ebony_log", MapColor.BLACK, MapColor.STONE_GRAY);
        }
    }

    public static class StrippedEbonyLogBlock extends CharmLogBlock {
        public StrippedEbonyLogBlock(CharmModule module) {
            super(module, "stripped_ebony_log", MapColor.BLACK, MapColor.BLACK);
        }
    }

    public static class StrippedEbonyWoodBlock extends CharmLogBlock {
        public StrippedEbonyWoodBlock(CharmModule module) {
            super(module, "stripped_ebony_wood", MapColor.BLACK, MapColor.BLACK);
        }
    }

    public static class EbonyPlanksBlock extends CharmPlanksBlock {
        public EbonyPlanksBlock(CharmModule module) {
            super(module, "ebony_planks", MapColor.BLACK);
        }
    }

    public static class EbonySaplingBlock extends CharmSaplingBlock {
        public EbonySaplingBlock(CharmModule module) {
            super(module, "ebony_sapling", new SaplingGenerator() {
                @Nullable
                @Override
                protected ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bees) {
                    return (ConfiguredFeature<TreeFeatureConfig, ?>) EbonyWood.TREE;
                }
            });
        }
    }

    public static class EbonySignBlock extends CharmSignBlock {
        public EbonySignBlock(CharmModule module) {
            super(module, "ebony_sign", EbonyWood.SIGN_TYPE, MapColor.BLACK);
        }

        @Override
        public void createBlockItem(Identifier id) {
            // no don't
        }
    }

    public static class EbonySlabBlock extends CharmSlabBlock {
        public EbonySlabBlock(CharmModule module) {
            super(module, "ebony_slab", MapColor.BLACK);
        }
    }

    public static class EbonyStairsBlock extends CharmStairsBlock {
        public EbonyStairsBlock(CharmModule module) {
            super(module, "ebony_stairs", EbonyWood.PLANKS);
        }
    }

    public static class EbonyWallSignBlock extends CharmWallSignBlock {
        public EbonyWallSignBlock(CharmModule module) {
            super(module, "ebony_wall_sign", EbonyWood.SIGN_BLOCK, EbonyWood.SIGN_TYPE, MapColor.BLACK);
        }
    }

    public static class EbonyWoodBlock extends CharmLogBlock {
        public EbonyWoodBlock(CharmModule module) {
            super(module, "ebony_wood", MapColor.BROWN);
        }
    }
}
