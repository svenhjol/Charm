package svenhjol.charm.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class MapHelper {
    public static ItemStack create(ServerLevel level, BlockPos pos, Component name, MapDecoration.Type targetType, int color) {
        ItemStack stack = MapItem.create(level, pos.getX(), pos.getZ(), (byte)2, true, true);
        MapItem.renderBiomePreviewMap(level, stack);
        MapItemSavedData.addTargetDecoration(stack, pos, "+", targetType);
        stack.setHoverName(name);

        CompoundTag nbt = ItemNbtHelper.getCompound(stack, "display");
        nbt.putInt("MapColor", color);
        ItemNbtHelper.setCompound(stack, "display", nbt);
        return stack;
    }
}
