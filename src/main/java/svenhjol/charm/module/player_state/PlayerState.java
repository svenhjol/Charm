package svenhjol.charm.module.player_state;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.PosHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.player_state.PlayerStateClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.BiConsumer;

@Module(mod = Charm.MOD_ID, alwaysEnabled = true, client = svenhjol.charm.module.player_state.PlayerStateClient.class, description = "Synchronize additional state from server to client.")
public class PlayerState extends CharmModule {
    public static final ResourceLocation MSG_SERVER_UPDATE_PLAYER_STATE = new ResourceLocation(Charm.MOD_ID, "server_update_player_state");
    public static List<BiConsumer<ServerPlayer, CompoundTag>> listeners = new ArrayList<>();

    @Config(name = "Server state update interval", description = "Interval (in ticks) on which additional world state will be synchronised to the client.")
    public static int serverStateInverval = 120;

    @Override
    public void register() {
        // register server message handler to call the serverCallback
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_UPDATE_PLAYER_STATE, this::handleServerUpdatePlayerState);
    }

    private void handleServerUpdatePlayerState(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf data, PacketSender sender) {
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
    public static void serverCallback(ServerPlayer player) {
        ServerLevel world = player.getLevel();
        BlockPos pos = player.blockPosition();
        long dayTime = world.getDayTime() % 24000;
        CompoundTag nbt = new CompoundTag();

        nbt.putBoolean("mineshaft", PosHelper.isInsideStructure(world, pos, StructureFeature.MINESHAFT));
        nbt.putBoolean("stronghold", PosHelper.isInsideStructure(world, pos, StructureFeature.STRONGHOLD));
        nbt.putBoolean("fortress", PosHelper.isInsideStructure(world, pos, StructureFeature.NETHER_BRIDGE));
        nbt.putBoolean("shipwreck", PosHelper.isInsideStructure(world, pos, StructureFeature.SHIPWRECK));
        nbt.putBoolean("village", world.isVillage(pos));
        nbt.putBoolean("day", dayTime > 0 && dayTime < 12700);

        // send updated player data to listeners
        listeners.forEach(action -> action.accept(player, nbt));

        // send updated player data to client
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        String serialized = null;

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            NbtIo.writeCompressed(nbt, out);
            serialized = Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            Charm.LOG.warn("Failed to compress player state");
        }

        if (serialized != null) {
            buffer.writeUtf(serialized);
            ServerPlayNetworking.send(player, PlayerStateClient.MSG_CLIENT_UPDATE_PLAYER_STATE, buffer);
        }
    }
}
