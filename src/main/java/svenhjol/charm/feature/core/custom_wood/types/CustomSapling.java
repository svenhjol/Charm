package svenhjol.charm.feature.core.custom_wood.types;

import net.minecraft.world.level.block.grower.TreeGrower;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;
import svenhjol.charm.foundation.block.CharmSaplingBlock;

import java.util.Optional;
import java.util.function.Supplier;

public class CustomSapling {
    public final Supplier<CharmSaplingBlock> block;
    public final Supplier<CharmSaplingBlock.BlockItem> item;

    public CustomSapling(CustomWoodHolder holder) {
        var material = holder.getMaterial();
        var saplingId = holder.getMaterialName() + "_sapling";

        var tree = material.tree();
        if (tree.isEmpty()) {
            holder.owner().log().error("No tree defined for sapling!");
        }

        // Param 1 = Mega tree (?), Param 2 = Normal tree, Param 3 = Some variant, like with beehives (?)
        var treeGrower = new TreeGrower(holder.getMaterialName(), Optional.empty(), tree, Optional.empty());

        block = holder.ownerRegistry().block(saplingId, () -> new CharmSaplingBlock(holder.getMaterial(), treeGrower));
        item = holder.ownerRegistry().item(saplingId, () -> new CharmSaplingBlock.BlockItem(block));

        holder.addCreativeTabItem(CustomType.SAPLING, item);
    }
}
