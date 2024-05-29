package svenhjol.charm.feature.redstone_sand.common;

import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.redstone_sand.RedstoneSand;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<RedstoneSand> {
    private static final String ID = "redstone_sand";
    public final Supplier<Block> block;
    public final Supplier<Block.BlockItem> blockItem;

    public Registers(RedstoneSand feature) {
        super(feature);

        var registry = feature().registry();
        block = registry.block(ID, Block::new);
        blockItem = registry.item(ID, () -> new Block.BlockItem(block));
    }
}
