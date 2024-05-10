package svenhjol.charm.feature.custom_wood.common;

import svenhjol.charm.foundation.block.CharmWoodenTrapdoorBlock;

import java.util.function.Supplier;

public class CustomTrapdoor {
    public final Supplier<CharmWoodenTrapdoorBlock> block;
    public final Supplier<CharmWoodenTrapdoorBlock.BlockItem> item;

    public CustomTrapdoor(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_trapdoor";
        block = holder.ownerRegistry().block(id, () -> new CharmWoodenTrapdoorBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CharmWoodenTrapdoorBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomWoodHelper.TRAPDOORS, item);
    }
}
