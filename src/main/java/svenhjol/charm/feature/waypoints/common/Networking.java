package svenhjol.charm.feature.waypoints.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.waypoints.Waypoints;
import svenhjol.charm.charmony.feature.FeatureHolder;

public final class Networking extends FeatureHolder<Waypoints> {
    public Networking(Waypoints feature) {
        super(feature);
    }

    // Server-to-client packet to update the client with fresh data.
    public record S2CUpdateWaypointInfo(BlockPos pos, Component title, DyeColor color, boolean playSound) implements CustomPacketPayload {
        public static Type<S2CUpdateWaypointInfo> TYPE = new Type<>(Charm.id("waypoint_info"));
        public static StreamCodec<FriendlyByteBuf, S2CUpdateWaypointInfo> CODEC =
            StreamCodec.of(S2CUpdateWaypointInfo::encode, S2CUpdateWaypointInfo::decode);

        public static void send(ServerPlayer player, WaypointData waypoint) {
            ServerPlayNetworking.send(player, new S2CUpdateWaypointInfo(
                waypoint.pos(),
                waypoint.title(),
                waypoint.color(),
                true)
            );
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void encode(FriendlyByteBuf buf, S2CUpdateWaypointInfo self) {
            buf.writeBlockPos(self.pos);
            NetworkHelper.encodeComponent(buf, self.title);
            buf.writeEnum(self.color);
            buf.writeBoolean(self.playSound);
        }

        private static S2CUpdateWaypointInfo decode(FriendlyByteBuf buf) {
            return new S2CUpdateWaypointInfo(
                buf.readBlockPos(),
                NetworkHelper.decodeComponent(buf),
                buf.readEnum(DyeColor.class),
                buf.readBoolean()
            );
        }
    }

    // Server-to-client empty packet to clear the last seen title and color on the client.
    public record S2CClearWaypointInfo() implements CustomPacketPayload {
        public static Type<S2CClearWaypointInfo> TYPE = new Type<>(Charm.id("clear_waypoint_info"));
        public static StreamCodec<FriendlyByteBuf, S2CClearWaypointInfo> CODEC =
            StreamCodec.of(S2CClearWaypointInfo::encode, S2CClearWaypointInfo::decode);

        public static void send(ServerPlayer player) {
            ServerPlayNetworking.send(player, new S2CClearWaypointInfo());
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }

        private static void encode(FriendlyByteBuf buf, S2CClearWaypointInfo self) {}

        private static S2CClearWaypointInfo decode(FriendlyByteBuf buf) {
            return new S2CClearWaypointInfo();
        }
    }
}
