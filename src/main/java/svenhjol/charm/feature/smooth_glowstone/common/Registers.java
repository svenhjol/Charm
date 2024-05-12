package svenhjol.charm.feature.smooth_glowstone.common;

import svenhjol.charm.feature.smooth_glowstone.SmoothGlowstone;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<SmoothGlowstone> {
    private static final String BLOCK_ID = "smooth_glowstone";

    public final Supplier<Block> block;
    public final Supplier<Block.BlockItem> blockItem;

    public Registers(SmoothGlowstone feature) {
        super(feature);

        block = feature.registry().block(BLOCK_ID, Block::new);
        blockItem = feature.registry().item(BLOCK_ID,
            () -> new Block.BlockItem(block.get()));
    }
}
