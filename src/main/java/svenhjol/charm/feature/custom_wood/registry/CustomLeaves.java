package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmLeavesBlock;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class CustomLeaves {
    public final Supplier<CharmLeavesBlock> block;
    public final Supplier<CharmLeavesBlock.BlockItem> item;

    public CustomLeaves(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_leaves";
        block = holder.getRegistry().block(id, () -> new CharmLeavesBlock(holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmLeavesBlock.BlockItem(block));

        holder.getRegistry().ignite(block); // Leaves can set on fire.
        holder.addCreativeTabItem(CustomWoodHelper.LEAVES, item);
    }
}
