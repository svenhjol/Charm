package svenhjol.charm.feature.smooth_glowstone.common;

import net.minecraft.world.level.block.Blocks;

public class Block extends net.minecraft.world.level.block.Block {
    public Block() {
        super(Properties.ofFullCopy(Blocks.GLOWSTONE));
    }

    public static class BlockItem extends net.minecraft.world.item.BlockItem {
        public BlockItem(Block block) {
            super(block, new Properties());
        }
    }
}
