package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomWoodenTrapdoorBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class TrapdoorHolder {
    public final Supplier<CustomWoodenTrapdoorBlock> block;
    public final Supplier<CustomWoodenTrapdoorBlock.BlockItem> item;

    public TrapdoorHolder(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_trapdoor";
        block = holder.ownerRegistry().block(id, () -> new CustomWoodenTrapdoorBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CustomWoodenTrapdoorBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomType.TRAPDOOR, item);
    }
}
