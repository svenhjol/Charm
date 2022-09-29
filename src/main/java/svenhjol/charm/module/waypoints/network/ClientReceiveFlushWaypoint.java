package svenhjol.charm.module.waypoints.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.network.ClientReceiver;
import svenhjol.charm.network.Id;
import svenhjol.charm.module.waypoints.WaypointsClient;

@Id("charm:flush_waypoint")
public class ClientReceiveFlushWaypoint extends ClientReceiver {
    @Override
    public void handle(Minecraft client, FriendlyByteBuf buffer) {
        client.execute(() -> {
            WaypointsClient.lastColor = null;
            WaypointsClient.lastMessage = null;
        });
    }
}
