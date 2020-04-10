package svenhjol.charm.base.compat;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockMatcher;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import javax.annotation.Nullable;

public class NetherModCompat {
    private static Tag<Block> netherrackTag = new BlockTags.Wrapper(new ResourceLocation("forge", "netherrack"));
    private static OreFeatureConfig.FillerBlockType fillerBlockType = OreFeatureConfig.FillerBlockType.create("NETHERRACK_TAG", "netherrack_tag", new BlockMatcher(Blocks.AIR){
        @Override
        public boolean test(@Nullable BlockState state) {
            return state != null && netherrackTag.contains(state.getBlock());
        }
    });

    public static Tag<Block> getNetherrackTag() {
        return netherrackTag;
    }

    public static OreFeatureConfig.FillerBlockType getNetherrackTaggedFillerBlockType() {
        return fillerBlockType;
    }
}
