package svenhjol.charm.feature.custom_wood.common;

import svenhjol.charm.foundation.block.CharmSlabBlock;

import java.util.function.Supplier;

public class CustomSlab {
    public final Supplier<CharmSlabBlock> block;
    public final Supplier<CharmSlabBlock.BlockItem> item;

    public CustomSlab(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_slab";

        block = holder.ownerRegistry().block(id, () -> new CharmSlabBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CharmSlabBlock.BlockItem(block));

        holder.ownerRegistry().ignite(block); // Slabs can set on fire.
        holder.addCreativeTabItem(CustomWoodHelper.SLABS, item);
    }
}
