package svenhjol.charm.feature.custom_wood.common;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.StairBlock;

import java.util.function.Supplier;

public class CustomStairs {
    public final Supplier<? extends StairBlock> block;
    public final Supplier<? extends BlockItem> item;

    public CustomStairs(CustomWoodHolder holder, CustomPlanks planks) {
        var id = holder.getMaterialName() + "_stairs";

        var stairs = holder.ownerRegistry().stairsBlock(id, holder::getMaterial,
            () -> planks.block.get().defaultBlockState());

        // Stairs can set on fire.
        holder.ownerRegistry().ignite(stairs.getFirst());

        // References seem a bit broken here. Why are stairs this?
        block = stairs.getFirst();
        item = stairs.getSecond();

        holder.addCreativeTabItem(CustomWoodHelper.STAIRS, item);
    }
}
