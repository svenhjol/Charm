package svenhjol.charm.module.waypoints.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.module.waypoints.Waypoints;
import svenhjol.charm.module.waypoints.WaypointsClient;
import svenhjol.charm.network.ClientReceiver;
import svenhjol.charm.network.Id;

@Id("charm:closest_waypoint")
public class ClientReceiveClosestWaypoint extends ClientReceiver {
    @Override
    public void handle(Minecraft client, FriendlyByteBuf buffer) {
        var title = buffer.readComponent();
        var color = buffer.readEnum(DyeColor.class);
        var playSound = buffer.readBoolean();

        client.execute(() -> {
            var minecraft = Minecraft.getInstance();

            if (minecraft == null || minecraft.player == null) return;
            var player = minecraft.player;
            var random = player.getRandom();

            // Prevent spamming the client if the message and color are the same.
            var message = title.getString();
            if (WaypointsClient.lastMessage != null
                && WaypointsClient.lastColor != null
                && WaypointsClient.lastMessage.equals(message)
                && WaypointsClient.lastColor.equals(color)) return;

            WaypointsClient.lastColor = color;
            WaypointsClient.lastMessage = message;

            var displayTitle = (MutableComponent) title;

            if (playSound) {
                minecraft.getSoundManager().play(SimpleSoundInstance.forUI(Waypoints.BROADCAST_SOUND, 0.9F + (0.2F * random.nextFloat()), 0.35F));
            }

            var textColor = color.getFireworkColor() | 0x171717;
            WaypointsClient.broadcastMessage = displayTitle.withStyle(style -> style.withColor(textColor));
            WaypointsClient.broadcastMessageTime = Waypoints.broadcastDuration * 60;
        });
    }
}
