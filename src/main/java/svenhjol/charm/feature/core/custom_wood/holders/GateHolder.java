package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomGateBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class GateHolder {
    public final Supplier<CustomGateBlock> block;
    public final Supplier<CustomGateBlock.BlockItem> item;

    public GateHolder(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_fence_gate";
        block = holder.ownerRegistry().block(id, () -> new CustomGateBlock(holder.getMaterial()));
        item = holder.ownerRegistry().item(id, () -> new CustomGateBlock.BlockItem(block));

        holder.ownerRegistry().ignite(block); // Gates can set on fire.
        holder.addCreativeTabItem(CustomType.GATE, item);
    }
}
