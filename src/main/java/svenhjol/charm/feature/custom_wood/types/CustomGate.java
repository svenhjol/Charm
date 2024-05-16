package svenhjol.charm.feature.custom_wood.types;

import svenhjol.charm.feature.custom_wood.common.CustomType;
import svenhjol.charm.feature.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmGateBlock;

import java.util.function.Supplier;

public class CustomGate {
    public final Supplier<CharmGateBlock> block;
    public final Supplier<CharmGateBlock.BlockItem> item;

    public CustomGate(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_fence_gate";
        block = holder.ownerRegistry().block(id, () -> new CharmGateBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CharmGateBlock.BlockItem(block));

        holder.ownerRegistry().ignite(block); // Gates can set on fire.
        holder.addCreativeTabItem(CustomType.GATE, item);
    }
}
