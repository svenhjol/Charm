package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmWoodenTrapdoorBlock;

import java.util.function.Supplier;

public class CustomTrapdoor {
    public final Supplier<CharmWoodenTrapdoorBlock> block;
    public final Supplier<CharmWoodenTrapdoorBlock.BlockItem> item;

    public CustomTrapdoor(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_trapdoor";
        block = holder.getRegistry().block(id, () -> new CharmWoodenTrapdoorBlock(holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmWoodenTrapdoorBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomWoodHelper.TRAPDOORS, item);
    }
}
