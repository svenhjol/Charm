package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmSlabBlock;

import java.util.function.Supplier;

public class SlabHolder {
    public final Supplier<CharmSlabBlock> block;
    public final Supplier<CharmSlabBlock.BlockItem> item;

    public SlabHolder(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_slab";

        block = holder.ownerRegistry().block(id, () -> new CharmSlabBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CharmSlabBlock.BlockItem(block));

        holder.ownerRegistry().ignite(block); // Slabs can set on fire.
        holder.addCreativeTabItem(CustomType.SLAB, item);
    }
}
