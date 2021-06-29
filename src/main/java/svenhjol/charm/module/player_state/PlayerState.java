package svenhjol.charm.module.player_state;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.api.CharmNetworkReferences;
import svenhjol.charm.api.CharmPlayerStateKeys;
import svenhjol.charm.helper.NetworkHelper;
import svenhjol.charm.helper.PosHelper;
import svenhjol.charm.module.CharmModule;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@Module(mod = Charm.MOD_ID, alwaysEnabled = true, client = PlayerStateClient.class, description = "Synchronize additional state from server to client.")
public class PlayerState extends CharmModule {
    public static final ResourceLocation MSG_SERVER_UPDATE_PLAYER_STATE
        = new ResourceLocation(CharmNetworkReferences.ServerUpdatePlayerState.getSerializedName());

    private static final List<BiConsumer<ServerPlayer, CompoundTag>> callbacks = new ArrayList<>();

    @Config(name = "Server state update interval", description = "Interval (in ticks) on which additional player state will be synchronised to the client.")
    public static int serverStateInverval = 120;

    @Override
    public void register() {
        // register server message handler to call the serverCallback
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_UPDATE_PLAYER_STATE, this::handleServerUpdatePlayerState);
    }

    public static void addNbtBeforeSending(BiConsumer<ServerPlayer, CompoundTag> callback) {
        callbacks.add(callback);
    }

    private void handleServerUpdatePlayerState(MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf data, PacketSender sender) {
        server.execute(() -> {
            if (player == null)
                return;

            serverCallback(player);
        });
    }

    /**
     * Populates an NBT tag of state information about the player.
     * Sends a compressed string of data to the client to unpack.
     *
     * Call this directly if you need state data immediately.
     */
    public static void serverCallback(ServerPlayer player) {
        ServerLevel world = player.getLevel();
        BlockPos pos = player.blockPosition();
        CompoundTag nbt = new CompoundTag();

        nbt.putBoolean(CharmPlayerStateKeys.InsideMineshaft.toString(), PosHelper.isInsideStructure(world, pos, StructureFeature.MINESHAFT));
        nbt.putBoolean(CharmPlayerStateKeys.InsideStronghold.toString(), PosHelper.isInsideStructure(world, pos, StructureFeature.STRONGHOLD));
        nbt.putBoolean(CharmPlayerStateKeys.InsideNetherFortress.toString(), PosHelper.isInsideStructure(world, pos, StructureFeature.NETHER_BRIDGE));
        nbt.putBoolean(CharmPlayerStateKeys.InsideShipwreck.toString(), PosHelper.isInsideStructure(world, pos, StructureFeature.SHIPWRECK));
        nbt.putBoolean(CharmPlayerStateKeys.InsideMineshaft.toString(), world.isVillage(pos));

        // allow other mods to update the nbt
        callbacks.forEach(action -> action.accept(player, nbt));

        // send updated player data to client
        FriendlyByteBuf buffer = NetworkHelper.encodeNbt(nbt);

        if (buffer != null)
            ServerPlayNetworking.send(player, PlayerStateClient.MSG_CLIENT_UPDATE_PLAYER_STATE, buffer);
    }
}
