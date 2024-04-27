package svenhjol.charm.foundation.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.foundation.annotation.Packet;
import svenhjol.charm.foundation.enums.PacketDirection;

public abstract class CharmPacket<T extends CustomPacketPayload> implements CustomPacketPayload {
    public abstract StreamCodec<FriendlyByteBuf, T> codec();

    @Override
    public Type<T> type() {
        String id;

        if (getClass().isAnnotationPresent(Packet.class)) {
            var annotation = getClass().getAnnotation(Packet.class);
            id = annotation.id();
        } else {
            throw new IllegalStateException("Missing ID for `" + getClass() + "`");
        }

        return CustomPacketPayload.createType(id);
    }

    public PacketDirection direction() {
        PacketDirection direction;

        if (getClass().isAnnotationPresent(Packet.class)) {
            var annotation = getClass().getAnnotation(Packet.class);
            direction = annotation.direction();
        } else {
            throw new IllegalStateException("Missing packet direction for `" + getClass() + "`");
        }

        return direction;
    }

    public Network network() {
        return switch (direction()) {
            case CLIENT_TO_SERVER -> Server.instance();
            case SERVER_TO_CLIENT -> Client.instance();
        };
    }

    public abstract void handle(Player player);
}
