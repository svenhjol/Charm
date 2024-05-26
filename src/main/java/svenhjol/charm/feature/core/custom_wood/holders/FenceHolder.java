package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomFenceBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class FenceHolder {
    public final Supplier<CustomFenceBlock> block;
    public final Supplier<CustomFenceBlock.BlockItem> item;

    public FenceHolder(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_fence";
        block = holder.ownerRegistry().block(id, () -> new CustomFenceBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CustomFenceBlock.BlockItem(block));

        holder.ownerRegistry().ignite(block); // Fences can set on fire.
        holder.addCreativeTabItem(CustomType.FENCE, item);
    }
}
