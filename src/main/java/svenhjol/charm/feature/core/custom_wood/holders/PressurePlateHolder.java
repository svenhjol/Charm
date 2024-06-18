package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomWoodenPressurePlateBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class PressurePlateHolder {
    public final Supplier<CustomWoodenPressurePlateBlock> block;
    public final Supplier<CustomWoodenPressurePlateBlock.BlockItem> item;

    public PressurePlateHolder(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_pressure_plate";

        block = holder.ownerRegistry().block(id, () -> new CustomWoodenPressurePlateBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CustomWoodenPressurePlateBlock.BlockItem(block));

        holder.addItemToCreativeTab(item, CustomType.PRESSURE_PLATE);
    }
}
