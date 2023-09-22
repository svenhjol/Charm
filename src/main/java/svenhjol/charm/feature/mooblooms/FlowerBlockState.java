package svenhjol.charm.feature.mooblooms;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SuspiciousEffectHolder;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public enum FlowerBlockState {
    ALLIUM(Blocks.ALLIUM.defaultBlockState()),
    AZURE_BLUET(Blocks.AZURE_BLUET.defaultBlockState()),
    BLUE_ORCHID(Blocks.BLUE_ORCHID.defaultBlockState()),
    CORNFLOWER(Blocks.CORNFLOWER.defaultBlockState()),
    DANDELION(Blocks.DANDELION.defaultBlockState()),
    LILY_OF_THE_VALLEY(Blocks.LILY_OF_THE_VALLEY.defaultBlockState()),
    ORANGE_TULIP(Blocks.ORANGE_TULIP.defaultBlockState()),
    OXEYE_DAISY(Blocks.OXEYE_DAISY.defaultBlockState()),
    PINK_PETALS(Blocks.PINK_PETALS.defaultBlockState()),
    PINK_TULIP(Blocks.PINK_TULIP.defaultBlockState()),
    POPPY(Blocks.POPPY.defaultBlockState()),
    RED_TULIP(Blocks.RED_TULIP.defaultBlockState()),
    SUNFLOWER(Blocks.SUNFLOWER.defaultBlockState()),
    WHITE_TULIP(Blocks.WHITE_TULIP.defaultBlockState());

    private final BlockState flowerBlockState;
    private static final int CHERRY_BLOSSOM_HEALING_DURATION = 4;
    private static final int SUNFLOWER_HEALTH_DURATION = 12;

    FlowerBlockState(BlockState flower) {
        this.flowerBlockState = flower;
    }

    public BlockState getBlockState() {
        return flowerBlockState;
    }

    public Block getBlock() {
        return flowerBlockState.getBlock();
    }

    public List<SuspiciousEffectHolder.EffectEntry> getEffects() {
        var block = flowerBlockState.getBlock();

        if (block instanceof FlowerBlock flowerBlock) {
            return flowerBlock.getSuspiciousEffects();
        } else if (this.equals(SUNFLOWER)) {
            return List.of(new SuspiciousEffectHolder.EffectEntry(MobEffects.HEALTH_BOOST, SUNFLOWER_HEALTH_DURATION * 20));
        } else if (this.equals(PINK_PETALS)) {
            return List.of(new SuspiciousEffectHolder.EffectEntry(MobEffects.HEAL, CHERRY_BLOSSOM_HEALING_DURATION * 20));
        }

        return List.of();
    }
}
