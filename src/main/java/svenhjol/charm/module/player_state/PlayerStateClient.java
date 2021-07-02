package svenhjol.charm.module.player_state;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.api.CharmNetworkReferences;
import svenhjol.charm.loader.CharmModule;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
@ClientModule(module = PlayerState.class)
public class PlayerStateClient extends CharmModule {
    public static final ResourceLocation MSG_CLIENT_UPDATE = new ResourceLocation(CharmNetworkReferences.ClientUpdatePlayerState.toString());

    @Override
    public void runWhenEnabled() {
        // send a state update request on a heartbeat
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (client.player != null && client.player.level.getGameTime() % PlayerState.heartbeat == 0)
                ClientPlayNetworking.send(PlayerState.MSG_SERVER_UPDATE, new FriendlyByteBuf(Unpooled.buffer()));
        });

        // Example: register client message handler to call the clientCallback
        ClientPlayNetworking.registerGlobalReceiver(MSG_CLIENT_UPDATE, this::handleUpdatePlayerState);
    }

    /**
     * Example: method to handle player state data sent from Charm.
     */
    private void handleUpdatePlayerState(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        // use NetworkHelper.decodeNbt to fetch data, do things with it
    }
}
