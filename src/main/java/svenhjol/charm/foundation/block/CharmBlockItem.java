package svenhjol.charm.foundation.block;

import net.minecraft.world.item.BlockItem;

import java.util.function.Supplier;

public abstract class CharmBlockItem<B extends CharmBlock<?>> extends BlockItem {
    public CharmBlockItem(Supplier<B> block, Properties properties) {
        super(block.get(), properties);
    }
}
