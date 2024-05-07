package svenhjol.charm.feature.smooth_glowstone;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SmoothGlowstoneBlock extends Block {
    public SmoothGlowstoneBlock() {
        super(Properties.ofFullCopy(Blocks.GLOWSTONE));
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(SmoothGlowstoneBlock block) {
            super(block, new Properties());
        }
    }
}
