package svenhjol.charm.feature.custom_wood.registry;

import net.minecraft.world.level.block.grower.TreeGrower;
import svenhjol.charm.feature.custom_wood.CustomWoodHelper;
import svenhjol.charm.feature.custom_wood.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmSaplingBlock;

import java.util.Optional;
import java.util.function.Supplier;

public class CustomSapling {
    public final Supplier<CharmSaplingBlock> block;
    public final Supplier<CharmSaplingBlock.BlockItem> item;

    public CustomSapling(CustomWoodHolder holder, float secondaryChance) {
        var saplingId = holder.getMaterialName() + "_sapling";
        var treeGrower = new TreeGrower(holder.getMaterialName(), secondaryChance, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());

        block = holder.getRegistry().block(saplingId, () -> new CharmSaplingBlock(holder.getMaterial(), treeGrower));
        item = holder.getRegistry().item(saplingId, () -> new CharmSaplingBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomWoodHelper.SAPLINGS, item);
    }
}
