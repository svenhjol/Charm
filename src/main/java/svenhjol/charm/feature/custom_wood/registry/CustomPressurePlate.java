package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmWoodenPressurePlateBlock;

import java.util.function.Supplier;

public class CustomPressurePlate {
    public final Supplier<CharmWoodenPressurePlateBlock> block;
    public final Supplier<CharmWoodenPressurePlateBlock.BlockItem> item;

    public CustomPressurePlate(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_pressure_plate";

        block = holder.getRegistry().block(id, () -> new CharmWoodenPressurePlateBlock(holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmWoodenPressurePlateBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomWoodHelper.PRESSURE_PLATES, item);
    }
}
