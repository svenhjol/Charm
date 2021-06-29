package svenhjol.charm.module.player_state;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.api.CharmNetworkReferences;
import svenhjol.charm.api.CharmPlayerStateKeys;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.helper.NetworkHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class PlayerStateClient extends CharmClientModule {
    public static final ResourceLocation MSG_CLIENT_UPDATE_PLAYER_STATE
        = new ResourceLocation(CharmNetworkReferences.ClientUpdatePlayerState.getSerializedName());

    // example properties set by the client callback.
    private static boolean insideMineshaft;

    public PlayerStateClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        // send a state update request on a heartbeat
        PlayerTickCallback.EVENT.register((player -> {
            if (player.level.isClientSide && player.level.getGameTime() % PlayerState.serverStateInverval == 0)
                ClientPlayNetworking.send(PlayerState.MSG_SERVER_UPDATE_PLAYER_STATE, new FriendlyByteBuf(Unpooled.buffer()));
        }));

        // register client message handler to call the clientCallback
        ClientPlayNetworking.registerGlobalReceiver(MSG_CLIENT_UPDATE_PLAYER_STATE, this::handleUpdatePlayerState);
    }

    private void handleUpdatePlayerState(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        CompoundTag nbt = NetworkHelper.decodeNbt(data);

        if (nbt != null)
            client.execute(() -> clientCallback(nbt));
    }

    /**
     * Example client callback to fetch properties sent from server.
     */
    @Environment(EnvType.CLIENT)
    public void clientCallback(CompoundTag data) {
        insideMineshaft = data.getBoolean(CharmPlayerStateKeys.InsideMineshaft.getSerializedName());
    }
}
