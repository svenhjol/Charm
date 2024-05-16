package svenhjol.charm.feature.core.custom_wood.types;

import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmWoodenButtonBlock;

import java.util.function.Supplier;

public class CustomButton {
    public final Supplier<CharmWoodenButtonBlock> block;
    public final Supplier<CharmWoodenButtonBlock.BlockItem> item;

    public CustomButton(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_button";

        block = holder.ownerRegistry().block(id, () -> new CharmWoodenButtonBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CharmWoodenButtonBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomType.BUTTON, item);
    }
}
