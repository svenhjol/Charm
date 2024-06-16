package svenhjol.charm.feature.waypoints.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.charmony.annotation.Packet;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.iface.PacketRequest;
import svenhjol.charm.feature.waypoints.Waypoints;

public final class Networking extends FeatureHolder<Waypoints> {
    public Networking(Waypoints feature) {
        super(feature);
    }

    // Server-to-client packet to update the client with fresh data.
    @Packet(
        id = "charm:waypoint_info"
    )
    public static class S2CUpdateWaypointInfo implements PacketRequest {
        private BlockPos pos;
        private Component title;
        private DyeColor color;
        private boolean playSound;
        
        public S2CUpdateWaypointInfo(BlockPos pos, Component title, DyeColor color, boolean playSound) {
            this.pos = pos;
            this.title = title;
            this.color = color;
            this.playSound = playSound;
        }
        
        public static void send(ServerPlayer player, WaypointData waypoint) {
            var message = new S2CUpdateWaypointInfo(
                waypoint.pos(),
                waypoint.title(),
                waypoint.color(),
                true);
            var buffer = message.newFriendlyByteBuf();
            message.encode(buffer);
            ServerPlayNetworking.send(player, message.id(), buffer);
        }

        public void encode(FriendlyByteBuf buf) {
            buf.writeBlockPos(this.pos);
            buf.writeComponent(this.title);
            buf.writeEnum(this.color);
            buf.writeBoolean(this.playSound);
        }

        public void decode(FriendlyByteBuf buf) {
            this.pos = buf.readBlockPos();
            this.title = buf.readComponent();
            this.color = buf.readEnum(DyeColor.class);
            this.playSound = buf.readBoolean();
        }
    }
    
    // Server-to-client empty packet to clear the last seen title and color on the client.
    @Packet(
        id = "charm:clear_waypoint_info"
    )
    public static class S2CClearWaypointInfo implements PacketRequest {
        public static void send(ServerPlayer player) {
            var message = new S2CClearWaypointInfo();
            var buffer = message.newFriendlyByteBuf();
            ServerPlayNetworking.send(player, message.id(), buffer);
        }

        public void encode(FriendlyByteBuf buf) {}

        public void decode(FriendlyByteBuf buf) {}
    }
}
