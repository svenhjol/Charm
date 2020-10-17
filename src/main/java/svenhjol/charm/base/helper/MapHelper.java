package svenhjol.charm.base.helper;

import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

public class MapHelper {
    public static ItemStack getMap(ServerWorld world, BlockPos pos, TranslatableText mapName, MapIcon.Type targetType, int color) {
        // generate the map
        ItemStack stack = FilledMapItem.createMap(world, pos.getX(), pos.getZ(), (byte) 2, true, true);
        FilledMapItem.fillExplorationMap(world, stack);
        MapState.addDecorationsTag(stack, pos, "+", targetType);
        stack.setCustomName(mapName);

        // set map color based on structure
        CompoundTag tag = ItemNBTHelper.getCompound(stack, "display");
        tag.putInt("MapColor", color);
        ItemNBTHelper.setCompound(stack, "display", tag);

        return stack;
    }
}
