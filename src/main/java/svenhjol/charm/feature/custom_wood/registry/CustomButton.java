package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmWoodenButtonBlock;

import java.util.function.Supplier;

public class CustomButton {
    public final Supplier<CharmWoodenButtonBlock> block;
    public final Supplier<CharmWoodenButtonBlock.BlockItem> item;

    public CustomButton(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_button";

        block = holder.getRegistry().block(id, () -> new CharmWoodenButtonBlock(holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmWoodenButtonBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomWoodHelper.BUTTONS, item);
    }
}
