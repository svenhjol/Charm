package svenhjol.charm.charmony.iface;

import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.charmony.annotation.Packet;

public interface PacketRequest {
    default void encode(FriendlyByteBuf buf) {
    }

    default void decode(FriendlyByteBuf buf) {
    }

    default ResourceLocation id() {
        if (this.getClass().isAnnotationPresent(Packet.class)) {
            Packet annotation = this.getClass().getAnnotation(Packet.class);
            return new ResourceLocation(annotation.id());
        } else {
            throw new IllegalStateException("Missing ID for `" + this.getClass() + "`");
        }
    }
    
    default FriendlyByteBuf newFriendlyByteBuf() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }
}
