package svenhjol.charm.feature.core.custom_wood.holders;

import net.minecraft.world.level.block.grower.TreeGrower;
import svenhjol.charm.feature.core.custom_wood.blocks.CustomSaplingBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.Optional;
import java.util.function.Supplier;

public class Sapling {
    public final Supplier<CustomSaplingBlock> block;
    public final Supplier<CustomSaplingBlock.BlockItem> item;

    public Sapling(CustomWoodHolder holder) {
        var material = holder.getMaterial();
        var saplingId = holder.getMaterialName() + "_sapling";

        var tree = material.tree();
        if (tree.isEmpty()) {
            holder.owner().log().error("No tree defined for sapling!");
        }

        // Param 1 = Mega tree (?), Param 2 = Normal tree, Param 3 = Some variant, like with beehives (?)
        var treeGrower = new TreeGrower(holder.getMaterialName(), Optional.empty(), tree, Optional.empty());

        block = holder.ownerRegistry().block(saplingId, () -> new CustomSaplingBlock(holder.getMaterial(), treeGrower));
        item = holder.ownerRegistry().item(saplingId, () -> new CustomSaplingBlock.BlockItem(block));

        holder.addItemToCreativeTab(item, CustomType.SAPLING);
    }
}
