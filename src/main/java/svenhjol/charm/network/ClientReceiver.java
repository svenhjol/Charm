package svenhjol.charm.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.helper.LogHelper;

import java.util.Optional;

/**
 * A server message received on the client.
 * Annotate a ServerSender with the same ID.
 */
@SuppressWarnings("unused")
public abstract class ClientReceiver {
    private ResourceLocation id; // cached message ID
    private int warnings = 0;

    public ClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(id(), this::handleInternal);
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

    private void handleInternal(Minecraft client, ClientPacketListener listener, FriendlyByteBuf buffer, PacketSender sender) {
        LogHelper.debug(getClass(), "Received message `" + id + "` from server.");

        try {
            handle(client, buffer);
        } catch (Exception e) {
            if (warnings < 10) {
                LogHelper.warn(getClass(), "Exception when handling message from client: " + e.getMessage());
                warnings++;
            }
        }
    }

    /**
     * Handle the message reading from the buffer and then executing on the client.
     * If exceptions are thrown here then they are caught by handleInternal.
     */
    public abstract void handle(Minecraft client, FriendlyByteBuf buffer);

    /**
     * Convenience method to read a wrapped optional compound tag from a buffer.
     */
    public Optional<CompoundTag> getCompoundTag(FriendlyByteBuf buffer) {
        return Optional.ofNullable(buffer.readNbt());
    }
}
