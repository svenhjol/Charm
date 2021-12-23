package svenhjol.charm.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.helper.LogHelper;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * A message sent from a client to the server.
 */
@SuppressWarnings("unused")
public abstract class ClientSender {
    private ResourceLocation id; // cached message ID

    /**
     * Send an empty message to the server.
     * Typically this is used to request the server send some specific data.
     */
    public void send() {
        send(null);
    }

    /**
     * Send message to server with packet data.
     */
    public void send(@Nullable Consumer<FriendlyByteBuf> callback) {
        var id = id();
        var buffer = new FriendlyByteBuf(Unpooled.buffer());

        if (callback != null) {
            callback.accept(buffer);
        }

        LogHelper.debug(getClass(), "Sending message `" + id + "` to server.");
        ClientPlayNetworking.send(id, buffer);
    }

    /**
     * Cache and fetch the message ID from the annotation.
     */
    private ResourceLocation id() {
        if (id == null) {
            if (getClass().isAnnotationPresent(Id.class)) {
                var annotation = getClass().getAnnotation(Id.class);
                id = new ResourceLocation(annotation.value());
            } else {
                throw new IllegalStateException("Missing ID for `" + getClass() + "`");
            }
        }

        return id;
    }
}
