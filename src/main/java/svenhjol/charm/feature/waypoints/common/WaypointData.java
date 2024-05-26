package svenhjol.charm.feature.waypoints.common;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.DyeColor;

public record WaypointData(BlockPos pos, Component title, DyeColor color) {
    public String makeHash(ServerLevel level) {
        return level.dimension().location().toString() + pos.asLong() + color.getName() + title.getString();
    }
}
