package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomPlanksBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class PlanksHolder {
    public final Supplier<CustomPlanksBlock> block;
    public final Supplier<CustomPlanksBlock.BlockItem> item;

    public PlanksHolder(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_planks";
        block = holder.ownerRegistry().block(id, () -> new CustomPlanksBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CustomPlanksBlock.BlockItem(block));

        holder.ownerRegistry().ignite(block); // Planks can set on fire.
        holder.addItemToCreativeTab(item, CustomType.PLANKS);
    }
}
