package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomWoodenDoorBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class DoorHolder {
    public final Supplier<CustomWoodenDoorBlock> block;
    public final Supplier<CustomWoodenDoorBlock.BlockItem> item;

    public DoorHolder(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_door";

        block = holder.ownerRegistry().block(id, () -> new CustomWoodenDoorBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CustomWoodenDoorBlock.BlockItem(block));

        holder.addItemToCreativeTab(item, CustomType.DOOR);
    }
}
