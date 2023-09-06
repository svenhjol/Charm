package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmDoorBlock;

import java.util.function.Supplier;

public class CustomDoor {
    public final Supplier<CharmDoorBlock> block;
    public final Supplier<CharmDoorBlock.BlockItem> item;

    public CustomDoor(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_door";

        block = holder.getRegistry().block(id, () -> new CharmDoorBlock(holder.getFeature(), holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmDoorBlock.BlockItem(holder.getFeature(), block));

        holder.addCreativeTabItem(CustomWoodHelper.DOORS, item);
    }
}
