package svenhjol.charm.feature.core.custom_wood.types;

import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmWoodenPressurePlateBlock;

import java.util.function.Supplier;

public class CustomPressurePlate {
    public final Supplier<CharmWoodenPressurePlateBlock> block;
    public final Supplier<CharmWoodenPressurePlateBlock.BlockItem> item;

    public CustomPressurePlate(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_pressure_plate";

        block = holder.ownerRegistry().block(id, () -> new CharmWoodenPressurePlateBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CharmWoodenPressurePlateBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomType.PRESSURE_PLATE, item);
    }
}
