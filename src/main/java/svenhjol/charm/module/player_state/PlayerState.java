package svenhjol.charm.module.player_state;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.player_state.network.ServerReceiveRequestState;
import svenhjol.charm.module.player_state.network.ServerSendState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
@CommonModule(mod = Charm.MOD_ID, alwaysEnabled = true, description = "Synchronize additional state from server to client.")
public class PlayerState extends CharmModule {
    public static final String WITHIN_STRUCTURES_TAG = "WithinStructures";
    public static final List<BiConsumer<ServerPlayer, CompoundTag>> CALLBACKS = new ArrayList<>();

    public static ServerSendState SERVER_SEND_STATE;
    public static ServerReceiveRequestState SERVER_RECEIVE_REQUEST_STATE;

    @Config(name = "Server state update interval", description = "Interval (in ticks) on which additional player state will be synchronised to the client.")
    public static int heartbeat = 120;

    @Override
    public void runWhenEnabled() {
        SERVER_SEND_STATE = new ServerSendState();
        SERVER_RECEIVE_REQUEST_STATE = new ServerReceiveRequestState();
    }

    public static void addCallback(BiConsumer<ServerPlayer, CompoundTag> callback) {
        CALLBACKS.add(callback);
    }
}
