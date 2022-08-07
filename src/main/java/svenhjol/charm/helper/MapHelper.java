package svenhjol.charm.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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

    public static ItemStack makeExplorerMap(ResourceLocation id, ServerLevel level, BlockPos pos, int distance, int color) {
        if (id.toString().startsWith("#")) {
            // Differentiating tags and raw strings is now deprecated - we only use tags now.
            id = new ResourceLocation(id.toString().substring(1));
        }

        LogHelper.debug(MapHelper.class, "Merchant wants to sell: " + id);
        var nearest = WorldHelper.findNearestStructure(id, level, pos, distance, true);
        if (nearest == null) return new ItemStack(Items.MAP);

        var name = TextHelper.translatable("structure." + id.getNamespace() + "." + id.getPath());

        if (name.getString().contains(".")) {
            name = TextHelper.translatable("filled_map.charm.structure");
        }

        var mapName = TextHelper.translatable("filled_map.charm.explorer_map", name);
        return create(level, nearest, mapName, MapDecoration.Type.TARGET_X, color);
    }
}
