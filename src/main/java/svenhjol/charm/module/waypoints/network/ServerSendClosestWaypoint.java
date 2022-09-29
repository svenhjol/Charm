package svenhjol.charm.module.waypoints.network;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerSender;

@Id("charm:closest_waypoint")
public class ServerSendClosestWaypoint extends ServerSender {
    public void send(ServerPlayer player, Component title, DyeColor color, boolean playSound) {
        super.send(player, buf -> {
            buf.writeComponent(title);
            buf.writeEnum(color);
            buf.writeBoolean(playSound);
        });
    }
}
