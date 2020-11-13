package svenhjol.charm.client;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.event.PlayerTickCallback;
import svenhjol.charm.module.PlayerState;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class PlayerStateClient extends CharmClientModule {
    public static final Identifier MSG_CLIENT_UPDATE_PLAYER_STATE = new Identifier(Charm.MOD_ID, "client_update_player_state");

    public boolean mineshaft = false;
    public boolean stronghold = false;
    public boolean fortress = false;
    public boolean shipwreck = false;
    public boolean village = false;
    public boolean isDaytime = true;

    public static PlayerStateClient INSTANCE;

    public PlayerStateClient(CharmModule module) {
        super(module);
        INSTANCE = this;
    }

    @Override
    public void register() {
        // send a state update request on a heartbeat (serverStateInterval)
        PlayerTickCallback.EVENT.register((player -> {
            if (player.world.isClient && player.world.getTime() % PlayerState.serverStateInverval == 0)
                ClientSidePacketRegistry.INSTANCE.sendToServer(PlayerState.MSG_SERVER_UPDATE_PLAYER_STATE, new PacketByteBuf(Unpooled.buffer()));
        }));

        // register client message handler to call the clientCallback
        ClientSidePacketRegistry.INSTANCE.register(MSG_CLIENT_UPDATE_PLAYER_STATE, (context, data) -> {
            CompoundTag tag = new CompoundTag();

            try {
                byte[] byteData = Base64.getDecoder().decode(data.readString());
                tag = NbtIo.readCompressed(new ByteArrayInputStream(byteData));
            } catch (IOException e) {
                CharmClient.LOG.warn("Failed to decompress player state");
            }

            CompoundTag finalTag = tag;
            context.getTaskQueue().execute(() -> {
                clientCallback(finalTag);
            });
        });
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
        this.isDaytime = data.getBoolean("day");
    }
}
