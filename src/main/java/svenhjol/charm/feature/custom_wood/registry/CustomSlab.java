package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmSlabBlock;

import java.util.function.Supplier;

public class CustomSlab {
    public final Supplier<CharmSlabBlock> block;
    public final Supplier<CharmSlabBlock.BlockItem> item;

    public CustomSlab(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_slab";

        block = holder.getRegistry().block(id, () -> new CharmSlabBlock(holder.getFeature(), holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmSlabBlock.BlockItem(holder.getFeature(), block));

        holder.getRegistry().ignite(block); // Slabs can set on fire.
        holder.addCreativeTabItem(CustomWoodHelper.SLABS, item);
    }
}
