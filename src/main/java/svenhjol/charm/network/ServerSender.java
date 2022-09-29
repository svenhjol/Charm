package svenhjol.charm.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.helper.LogHelper;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * A message sent from the server to a client or all connected clients.
 */
@SuppressWarnings("unused")
public abstract class ServerSender {
    protected ResourceLocation id; // cached message ID

    /**
     * Cache and fetch the message ID from the annotation.
     */
    protected ResourceLocation id() {
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

    protected void debug(String message) {
        if (showDebugMessages()) {
            LogHelper.debug(getClass(), message);
        }
    }

    protected boolean showDebugMessages() {
        return false;
    }

    /**
     * Send an empty message to a player client.
     * Typically this is used to request that the client perform a specific action.
     */
    public void send(ServerPlayer player) {
        send(player, null);
    }

    /**
     * Send message with packet data to a player client.
     */
    public void send(ServerPlayer player, @Nullable Consumer<FriendlyByteBuf> callback) {
        var id = id();
        var buffer = new FriendlyByteBuf(Unpooled.buffer());

        if (callback != null) {
            callback.accept(buffer);
        }

        debug("Sending message `" + id + "` to " + player.getUUID());
        ServerPlayNetworking.send(player, id, buffer);
    }

    /**
     * Send an empty message to all connected player clients.
     */
    public void sendToAll(MinecraftServer server) {
        sendToAll(server, null);
    }

    /**
     * Send message with packet data to all connected player clients.
     */
    public void sendToAll(MinecraftServer server, @Nullable Consumer<FriendlyByteBuf> callback) {
        var playerList = server.getPlayerList();
        playerList.getPlayers().forEach(player -> send(player, callback));
    }
}
