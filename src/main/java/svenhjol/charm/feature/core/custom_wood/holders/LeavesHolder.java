package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomLeavesBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class LeavesHolder {
    public final Supplier<CustomLeavesBlock> block;
    public final Supplier<CustomLeavesBlock.BlockItem> item;

    public LeavesHolder(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_leaves";
        block = holder.ownerRegistry().block(id, () -> new CustomLeavesBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CustomLeavesBlock.BlockItem(block));

        holder.ownerRegistry().ignite(block); // Leaves can set on fire.
        holder.addItemToCreativeTab(item, CustomType.LEAVES);
    }
}
