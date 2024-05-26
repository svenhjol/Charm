package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomWoodenButtonBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class ButtonHolder {
    public final Supplier<CustomWoodenButtonBlock> block;
    public final Supplier<CustomWoodenButtonBlock.BlockItem> item;

    public ButtonHolder(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_button";

        block = holder.ownerRegistry().block(id, () -> new CustomWoodenButtonBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CustomWoodenButtonBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomType.BUTTON, item);
    }
}
