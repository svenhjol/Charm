package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmFenceGateBlock;

import java.util.function.Supplier;

public class CustomGate {
    public final Supplier<CharmFenceGateBlock> block;
    public final Supplier<CharmFenceGateBlock.BlockItem> item;

    public CustomGate(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_fence_gate";
        block = holder.getRegistry().block(id, () -> new CharmFenceGateBlock(holder.getFeature(), holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmFenceGateBlock.BlockItem(holder.getFeature(), block));

        holder.getRegistry().ignite(block); // Gates can set on fire.
        holder.addCreativeTabItem(CustomWoodHelper.GATES, item);
    }
}
