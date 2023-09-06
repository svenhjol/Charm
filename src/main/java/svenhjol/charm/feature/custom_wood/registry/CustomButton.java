package svenhjol.charm.feature.custom_wood.registry;

import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charmony.block.CharmWoodButtonBlock;

import java.util.function.Supplier;

public class CustomButton {
    public final Supplier<CharmWoodButtonBlock> block;
    public final Supplier<CharmWoodButtonBlock.BlockItem> item;

    public CustomButton(CustomWoodHolder holder) {
        var id = holder.getMaterialName() + "_button";

        block = holder.getRegistry().block(id, () -> new CharmWoodButtonBlock(holder.getFeature(), holder.getMaterial()));
        item = holder.getRegistry().item(id, () -> new CharmWoodButtonBlock.BlockItem(holder.getFeature(), block));

        holder.addCreativeTabItem(CustomWoodHelper.BUTTONS, item);
    }
}
