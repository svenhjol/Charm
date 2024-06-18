package svenhjol.charm.feature.core.custom_wood.holders;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.StairBlock;
import svenhjol.charm.feature.core.custom_wood.common.CustomType;
import svenhjol.charm.feature.core.custom_wood.common.CustomWoodHolder;

import java.util.function.Supplier;

public class StairsHolder {
    public final Supplier<? extends StairBlock> block;
    public final Supplier<? extends BlockItem> item;

    public StairsHolder(CustomWoodHolder holder, PlanksHolder planks) {
        var id = holder.getMaterialName() + "_stairs";

        var stairs = holder.ownerRegistry().stairsBlock(id, holder::getMaterial,
            () -> planks.block.get().defaultBlockState());

        // Stairs can set on fire.
        holder.ownerRegistry().ignite(stairs.getFirst());

        // References seem a bit broken here. Why are stairs this?
        block = stairs.getFirst();
        item = stairs.getSecond();

        holder.addItemToCreativeTab(item, CustomType.STAIRS);
    }
}
