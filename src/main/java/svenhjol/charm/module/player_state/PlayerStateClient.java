package svenhjol.charm.module.player_state;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.module.player_state.PlayerState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class PlayerStateClient extends CharmClientModule {
    public static final ResourceLocation MSG_CLIENT_UPDATE_PLAYER_STATE = new ResourceLocation(Charm.MOD_ID, "client_update_player_state");

    public boolean mineshaft = false;
    public boolean stronghold = false;
    public boolean fortress = false;
    public boolean shipwreck = false;
    public boolean village = false;
    public boolean ruin = false;
    public boolean isDaytime = true;

    public static PlayerStateClient INSTANCE;

    public PlayerStateClient(CharmModule module) {
        super(module);
        INSTANCE = this;
    }

    @Override
    public void register() {
        // register client message handler to call the clientCallback
        ClientPlayNetworking.registerGlobalReceiver(MSG_CLIENT_UPDATE_PLAYER_STATE, this::handleClientUpdatePlayerState);
    }

    @Override
    public void init() {
        // send a state update request on a heartbeat (serverStateInterval)
        PlayerTickCallback.EVENT.register((player -> {
            if (player.level.isClientSide && player.level.getGameTime() % svenhjol.charm.module.player_state.PlayerState.serverStateInverval == 0)
                ClientPlayNetworking.send(PlayerState.MSG_SERVER_UPDATE_PLAYER_STATE, new FriendlyByteBuf(Unpooled.buffer()));
        }));
    }

    private void handleClientUpdatePlayerState(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        CompoundTag nbt = new CompoundTag();

        try {
            byte[] byteData = Base64.getDecoder().decode(data.readUtf());
            nbt = NbtIo.readCompressed(new ByteArrayInputStream(byteData));
        } catch (IOException e) {
            CharmClient.LOG.warn("Failed to decompress player state");
        }

        CompoundTag finalNbt = nbt;
        client.execute(() -> clientCallback(finalNbt));
    }

    /**
     * Unpack the received server data from the NBT tag.
     */
    @Environment(EnvType.CLIENT)
    public void clientCallback(CompoundTag data) {
        this.mineshaft = data.getBoolean("mineshaft");
        this.stronghold = data.getBoolean("stronghold");
        this.fortress = data.getBoolean("fortress");
        this.shipwreck = data.getBoolean("shipwreck");
        this.village = data.getBoolean("village");
        this.ruin = data.getBoolean("ruin");
        this.isDaytime = data.getBoolean("day");
    }
}
