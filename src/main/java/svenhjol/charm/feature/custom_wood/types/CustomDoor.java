package svenhjol.charm.feature.custom_wood.types;

import svenhjol.charm.feature.custom_wood.common.CustomType;
import svenhjol.charm.feature.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmWoodenDoorBlock;

import java.util.function.Supplier;

public class CustomDoor {
    public final Supplier<CharmWoodenDoorBlock> block;
    public final Supplier<CharmWoodenDoorBlock.BlockItem> item;

    public CustomDoor(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_door";

        block = holder.ownerRegistry().block(id, () -> new CharmWoodenDoorBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CharmWoodenDoorBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomType.DOOR, item);
    }
}
