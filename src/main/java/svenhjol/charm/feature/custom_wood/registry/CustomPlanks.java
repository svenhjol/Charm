package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmPlanksBlock;

import java.util.function.Supplier;

public class CustomPlanks {
    public final Supplier<CharmPlanksBlock> block;
    public final Supplier<CharmPlanksBlock.BlockItem> item;

    public CustomPlanks(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_planks";

        block = holder.getRegistry().block(id, () -> new CharmPlanksBlock(holder.getFeature(), holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmPlanksBlock.BlockItem(holder.getFeature(), block));

        holder.getRegistry().ignite(block); // Planks can set on fire.
        holder.addCreativeTabItem(CustomWoodHelper.PLANKS, item);
    }
}
