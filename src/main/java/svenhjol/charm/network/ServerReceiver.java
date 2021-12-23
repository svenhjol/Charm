package svenhjol.charm.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import svenhjol.charm.helper.LogHelper;

import java.util.Optional;

/**
 * A client message received on the server.
 * Annotate a ClientSender with the same ID.
 */
@SuppressWarnings("unused")
public abstract class ServerReceiver {
    private int warnings = 0;
    private ResourceLocation id; // cached message ID

    public ServerReceiver() {
        var id = id();
        ServerPlayNetworking.registerGlobalReceiver(id, this::handleInternal);
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

    protected void debug(String message) {
        if (showDebugMessages()) {
            LogHelper.debug(getClass(), message);
        }
    }

    protected boolean showDebugMessages() {
        return true;
    }

    private void handleInternal(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl listener, FriendlyByteBuf buffer, PacketSender sender) {
        var id = id();
        debug("Received message `" + id + "` from client " + player.getUUID() + ".");

        try {
            handle(server, player, buffer);
        } catch (Exception e) {
            if (warnings < 10) {
                debug("Exception when handling message from server: " + e.getMessage());
                warnings++;
            }
        }
    }
    /**
     * Handle the message reading from the buffer and then executing on the client.
     * If exceptions are thrown here then they are caught by handleInternal.
     */
    public abstract void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buffer);

    /**
     * Convenience method to read a wrapped optional compound tag from a buffer.
     */
    public Optional<CompoundTag> getCompoundTag(FriendlyByteBuf buffer) {
        return Optional.ofNullable(buffer.readNbt());
    }
}
