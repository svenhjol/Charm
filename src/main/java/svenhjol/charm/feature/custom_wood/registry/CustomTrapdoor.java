package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmTrapdoorBlock;

import java.util.function.Supplier;

public class CustomTrapdoor {
    public final Supplier<CharmTrapdoorBlock> block;
    public final Supplier<CharmTrapdoorBlock.BlockItem> item;

    public CustomTrapdoor(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_trapdoor";
        block = holder.getRegistry().block(id, () -> new CharmTrapdoorBlock(holder.getFeature(), holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmTrapdoorBlock.BlockItem(holder.getFeature(), block));

        holder.addCreativeTabItem(CustomWoodHelper.TRAPDOORS, item);
    }
}
