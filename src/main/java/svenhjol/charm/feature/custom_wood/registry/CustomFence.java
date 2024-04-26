package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmFenceBlock;

import java.util.function.Supplier;

public class CustomFence {
    public final Supplier<CharmFenceBlock> block;
    public final Supplier<CharmFenceBlock.BlockItem> item;

    public CustomFence(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_fence";
        block = holder.getRegistry().block(id, () -> new CharmFenceBlock(holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmFenceBlock.BlockItem(block));

        holder.getRegistry().ignite(block); // Fences can set on fire.
        holder.addCreativeTabItem(CustomWoodHelper.FENCES, item);
    }
}
