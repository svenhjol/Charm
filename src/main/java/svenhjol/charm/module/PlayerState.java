package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.StructureFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.client.PlayerStateClient;
import svenhjol.meson.event.PlayerTickCallback;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.PosHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.BiConsumer;

@Module(description = "Synchronize additional state from server to client.", alwaysEnabled = true)
public class PlayerState extends MesonModule {
    public static final Identifier MSG_SERVER_UPDATE_PLAYER_STATE = new Identifier(Charm.MOD_ID, "server_update_player_state");
    public static final Identifier MSG_CLIENT_UPDATE_PLAYER_STATE = new Identifier(Charm.MOD_ID, "client_update_player_state");
    public static List<BiConsumer<ServerPlayerEntity, CompoundTag>> listeners = new ArrayList<>();

    public static PlayerStateClient client;

    @Config(name = "Server state update interval", description = "Interval (in ticks) on which additional world state will be synchronised to the client.")
    public static int serverStateInverval = 120;

    @Override
    public void init() {

        // register server message handler to call the serverCallback
        ServerSidePacketRegistry.INSTANCE.register(MSG_SERVER_UPDATE_PLAYER_STATE, (context, data) -> {
            context.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
                if (player == null)
                    return;

                serverCallback(player);
            });
        });
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void initClient() {
        client = new PlayerStateClient();

        // send a state update request on a heartbeat (serverStateInterval)
        PlayerTickCallback.EVENT.register((player -> {
            if (player.world.isClient && player.world.getTime() % serverStateInverval == 0)
                ClientSidePacketRegistry.INSTANCE.sendToServer(MSG_SERVER_UPDATE_PLAYER_STATE, new PacketByteBuf(Unpooled.buffer()));
        }));

        // register client message handler to call the clientCallback
        ClientSidePacketRegistry.INSTANCE.register(MSG_CLIENT_UPDATE_PLAYER_STATE, (context, data) -> {
            CompoundTag tag = new CompoundTag();

            try {
                byte[] byteData = Base64.getDecoder().decode(data.readString());
                tag = NbtIo.readCompressed(new ByteArrayInputStream(byteData));
            } catch (IOException e) {
                Meson.LOG.warn("Failed to decompress player state");
            }

            CompoundTag finalTag = tag;
            context.getTaskQueue().execute(() -> {
                clientCallback(finalTag);
            });
        });
    }

    /**
     * Populates an NBT tag of state information about the player,
     * sends a compressed string of data to the client to unpack.
     */
    public static void serverCallback(ServerPlayerEntity player) {
        ServerWorld world = player.getServerWorld();
        BlockPos pos = player.getBlockPos();
        long dayTime = world.getTimeOfDay() % 24000;
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("mineshaft", PosHelper.isInsideStructure(world, pos, StructureFeature.MINESHAFT));
        tag.putBoolean("stronghold", PosHelper.isInsideStructure(world, pos, StructureFeature.STRONGHOLD));
        tag.putBoolean("fortress", PosHelper.isInsideStructure(world, pos, StructureFeature.FORTRESS));
        tag.putBoolean("shipwreck", PosHelper.isInsideStructure(world, pos, StructureFeature.SHIPWRECK));
        tag.putBoolean("village", world.isNearOccupiedPointOfInterest(pos));
        tag.putBoolean("day", dayTime > 0 && dayTime < 12700);

        // send updated player data to listeners
        listeners.forEach(action -> action.accept(player, tag));

        // send updated player data to client
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        String serialized = null;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            NbtIo.writeCompressed(tag, out);
            serialized = Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            Meson.LOG.warn("Failed to compress player state");
        }

        if (serialized != null) {
            buffer.writeString(serialized);
            ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, MSG_CLIENT_UPDATE_PLAYER_STATE, buffer);
        }
    }

    /**
     * Unpack the received server data from the NBT tag.
     */
    @Environment(EnvType.CLIENT)
    public static void clientCallback(CompoundTag data) {
        client.mineshaft = data.getBoolean("mineshaft");
        client.stronghold = data.getBoolean("stronghold");
        client.fortress = data.getBoolean("fortress");
        client.shipwreck = data.getBoolean("shipwreck");
        client.village = data.getBoolean("village");
        client.isDaytime = data.getBoolean("day");
    }
}
