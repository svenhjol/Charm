package svenhjol.charm.feature.core.custom_wood.types;

import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmLogBlock;

import java.util.function.Supplier;

public class CustomLogBlock {
    public final Supplier<CharmLogBlock> block;
    public final Supplier<CharmLogBlock.BlockItem> item;
    public final Supplier<CharmLogBlock> strippedBlock;
    public final Supplier<CharmLogBlock.BlockItem> strippedItem;

    public CustomLogBlock(CustomWoodHolder holder) {
        var material = holder.getMaterial();
        var registry = holder.ownerRegistry();

        var id = holder.getMaterialName() + "_log";
        var strippedId = "stripped_" + holder.getMaterialName() + "_log";

        block = registry.block(id, () -> new CharmLogBlock(material));
        item = registry.item(id, () -> new CharmLogBlock.BlockItem(block));
        strippedBlock = registry.block(strippedId, () -> new CharmLogBlock(material));
        strippedItem = registry.item(strippedId, () -> new CharmLogBlock.BlockItem(strippedBlock));

        // Logs can set on fire.
        registry.ignite(block);
        registry.ignite(strippedBlock);

        // Logs can be stripped.
        registry.strippable(block, strippedBlock);

        holder.addCreativeTabItem(CustomType.LOG, item);
        holder.addCreativeTabItem(CustomType.STRIPPED_LOG, strippedItem);
    }
}
