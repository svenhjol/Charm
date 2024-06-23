package svenhjol.charm.feature.core.custom_wood.holders;

import svenhjol.charm.feature.core.custom_wood.blocks.CustomLadderBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class LadderHolder {
    public final Supplier<CustomLadderBlock> block;
    public final Supplier<CustomLadderBlock.BlockItem> item;

    public LadderHolder(CustomWoodHolder holder) {
        var registry = holder.ownerRegistry();
        var material = holder.getMaterial();
        var id = holder.getMaterialName() + "_ladder";

        block = registry.block(id, () -> new CustomLadderBlock(material));
        item = registry.item(id, () -> new CustomLadderBlock.BlockItem(block));

        // Ladders can be used as furnace fuel.
        registry.fuel(item);

        // Add to creative menu.
        holder.addItemToCreativeTab(item, CustomType.LADDER);
    }
}
