package svenhjol.charm.feature.custom_wood.common;

import svenhjol.charm.foundation.block.CharmPlanksBlock;

import java.util.function.Supplier;

public class CustomPlanks {
    public final Supplier<CharmPlanksBlock> block;
    public final Supplier<CharmPlanksBlock.BlockItem> item;

    public CustomPlanks(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_planks";
        block = holder.ownerRegistry().block(id, () -> new CharmPlanksBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CharmPlanksBlock.BlockItem(block));

        holder.ownerRegistry().ignite(block); // Planks can set on fire.
        holder.addCreativeTabItem(CustomWoodHelper.PLANKS, item);
    }
}