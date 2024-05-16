package svenhjol.charm.feature.core.custom_wood.types;

import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmFenceBlock;

import java.util.function.Supplier;

public class CustomFence {
    public final Supplier<CharmFenceBlock> block;
    public final Supplier<CharmFenceBlock.BlockItem> item;

    public CustomFence(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_fence";
        block = holder.ownerRegistry().block(id, () -> new CharmFenceBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CharmFenceBlock.BlockItem(block));

        holder.ownerRegistry().ignite(block); // Fences can set on fire.
        holder.addCreativeTabItem(CustomType.FENCE, item);
    }
}
