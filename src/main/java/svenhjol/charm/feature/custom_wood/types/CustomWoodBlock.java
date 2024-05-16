package svenhjol.charm.feature.custom_wood.types;

import svenhjol.charm.feature.custom_wood.common.CustomType;
import svenhjol.charm.feature.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmLogBlock;

import java.util.function.Supplier;

public class CustomWoodBlock {
    public final Supplier<CharmLogBlock> block;
    public final Supplier<CharmLogBlock.BlockItem> item;
    public final Supplier<CharmLogBlock> strippedBlock;
    public final Supplier<CharmLogBlock.BlockItem> strippedItem;

    public CustomWoodBlock(CustomWoodHolder holder) {
        var material = holder.getMaterial();
        var registry = holder.ownerRegistry();

        var woodId = holder.getMaterialName() + "_wood";
        var strippedWoodId = "stripped_" + holder.getMaterialName() + "_wood";

        block = registry.block(woodId, () -> new CharmLogBlock(material));
        item = registry.item(woodId, () -> new CharmLogBlock.BlockItem(block));
        strippedBlock = registry.block(strippedWoodId, () -> new CharmLogBlock(material));
        strippedItem = registry.item(strippedWoodId, () -> new CharmLogBlock.BlockItem(strippedBlock));

        // Wood can set on fire.
        registry.ignite(block);
        registry.ignite(strippedBlock);

        // Wood can be stripped.
        registry.strippable(block, strippedBlock);

        holder.addCreativeTabItem(CustomType.WOOD, item);
        holder.addCreativeTabItem(CustomType.STRIPPED_WOOD, strippedItem);
    }
}
