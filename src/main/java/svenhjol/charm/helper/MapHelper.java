package svenhjol.charm.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapHelper {
    public static ItemStack getMap(ServerLevel world, BlockPos pos, TranslatableComponent mapName, MapDecoration.Type targetType, int color) {
        // generate the map
        ItemStack stack = MapItem.create(world, pos.getX(), pos.getZ(), (byte) 2, true, true);
        MapItem.renderBiomePreviewMap(world, stack);
        MapItemSavedData.addTargetDecoration(stack, pos, "+", targetType);
        stack.setHoverName(mapName);

        // set map color based on structure
        CompoundTag nbt = ItemNbtHelper.getCompound(stack, "display");
        nbt.putInt("MapColor", color);
        ItemNbtHelper.setCompound(stack, "display", nbt);

        return stack;
    }
}
