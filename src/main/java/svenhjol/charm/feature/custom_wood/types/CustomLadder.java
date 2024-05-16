package svenhjol.charm.feature.custom_wood.types;

import svenhjol.charm.feature.custom_wood.common.CustomType;
import svenhjol.charm.feature.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmLadderBlock;

import java.util.function.Supplier;

public class CustomLadder {
    public final Supplier<CharmLadderBlock> block;
    public final Supplier<CharmLadderBlock.BlockItem> item;

    public CustomLadder(CustomWoodHolder holder) {
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = holder.getMaterialName() + "_ladder";

        block = registry.block(id, () -> new CharmLadderBlock(material));
        item = registry.item(id, () -> new CharmLadderBlock.BlockItem(block));

        // Ladders can be used as furnace fuel.
        registry.fuel(item);

        // Add to creative menu.
        holder.addCreativeTabItem(CustomType.LADDER, item);
    }
}
