package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmGateBlock;

import java.util.function.Supplier;

public class CustomGate {
    public final Supplier<CharmGateBlock> block;
    public final Supplier<CharmGateBlock.BlockItem> item;

    public CustomGate(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_fence_gate";
        block = holder.getRegistry().block(id, () -> new CharmGateBlock(holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmGateBlock.BlockItem(block));

        holder.getRegistry().ignite(block); // Gates can set on fire.
        holder.addCreativeTabItem(CustomWoodHelper.GATES, item);
    }
}
