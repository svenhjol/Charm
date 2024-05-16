package svenhjol.charm.feature.core.custom_wood.types;

import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
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
        holder.addCreativeTabItem(CustomType.PLANKS, item);
    }
}
