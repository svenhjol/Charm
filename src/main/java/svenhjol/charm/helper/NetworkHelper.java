package svenhjol.charm.helper;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.Nullable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.function.Consumer;

/**
 * @version 1.0.1-charm
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class NetworkHelper {
    @Nullable
    public static FriendlyByteBuf encodeNbt(CompoundTag nbt) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            NbtIo.writeCompressed(nbt, out);
            String serialized = Base64.getEncoder().encodeToString(out.toByteArray());
            buffer.writeUtf(serialized);
            return buffer;
        } catch (IOException e) {
            LogHelper.warn(NetworkHelper.class, "Failed to compress nbt");
        }

        return null;
    }

    @Nullable
    public static CompoundTag decodeNbt(FriendlyByteBuf data) {
        try {
            byte[] byteData = Base64.getDecoder().decode(data.readUtf());
            return NbtIo.readCompressed(new ByteArrayInputStream(byteData));
        } catch (IOException e) {
            LogHelper.warn(NetworkHelper.class, "Failed to decompress nbt");
        }

        return null;
    }

    public static void sendEmptyPacketToServer(ResourceLocation id) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        ClientPlayNetworking.send(id, buffer);
    }

    public static void sendPacketToServer(ResourceLocation id, Consumer<FriendlyByteBuf> callback) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        callback.accept(buffer);
        ClientPlayNetworking.send(id, buffer);
    }

    public static void sendEmptyPacketToClient(ServerPlayer player, ResourceLocation id) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        ServerPlayNetworking.send(player, id, buffer);
    }

    public static void sendPacketToClient(ServerPlayer player, ResourceLocation id, Consumer<FriendlyByteBuf> callback) {
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        callback.accept(buffer);
        ServerPlayNetworking.send(player, id, buffer);
    }

    public static void sendPacketToAllClients(MinecraftServer server, ResourceLocation id, Consumer<FriendlyByteBuf> callback) {
        var playerList = server.getPlayerList();
        playerList.getPlayers().forEach(player -> sendPacketToClient(player, id, callback));
    }
}
