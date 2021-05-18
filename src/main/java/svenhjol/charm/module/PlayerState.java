package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.StructureFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.PosHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.PlayerStateClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.BiConsumer;

@Module(mod = Charm.MOD_ID, alwaysEnabled = true, client = PlayerStateClient.class, description = "Synchronize additional state from server to client.")
public class PlayerState extends CharmModule {
    public static final Identifier MSG_SERVER_UPDATE_PLAYER_STATE = new Identifier(Charm.MOD_ID, "server_update_player_state");
    public static List<BiConsumer<ServerPlayerEntity, NbtCompound>> listeners = new ArrayList<>();

    @Config(name = "Server state update interval", description = "Interval (in ticks) on which additional world state will be synchronised to the client.")
    public static int serverStateInverval = 120;

    @Override
    public void register() {
        // register server message handler to call the serverCallback
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_UPDATE_PLAYER_STATE, this::handleServerUpdatePlayerState);
    }

    private void handleServerUpdatePlayerState(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        server.execute(() -> {
            if (player == null)
                return;

            serverCallback(player);
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
        NbtCompound nbt = new NbtCompound();

        nbt.putBoolean("mineshaft", PosHelper.isInsideStructure(world, pos, StructureFeature.MINESHAFT));
        nbt.putBoolean("stronghold", PosHelper.isInsideStructure(world, pos, StructureFeature.STRONGHOLD));
        nbt.putBoolean("fortress", PosHelper.isInsideStructure(world, pos, StructureFeature.FORTRESS));
        nbt.putBoolean("shipwreck", PosHelper.isInsideStructure(world, pos, StructureFeature.SHIPWRECK));
        nbt.putBoolean("village", world.isNearOccupiedPointOfInterest(pos));
        nbt.putBoolean("day", dayTime > 0 && dayTime < 12700);

        // send updated player data to listeners
        listeners.forEach(action -> action.accept(player, nbt));

        // send updated player data to client
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        String serialized = null;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            NbtIo.writeCompressed(nbt, out);
            serialized = Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            Charm.LOG.warn("Failed to compress player state");
        }

        if (serialized != null) {
            buffer.writeString(serialized);
            ServerPlayNetworking.send(player, PlayerStateClient.MSG_CLIENT_UPDATE_PLAYER_STATE, buffer);
        }
    }
}
