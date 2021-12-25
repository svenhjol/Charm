package svenhjol.charm.module.player_state;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.module.player_state.network.ClientReceiveState;
import svenhjol.charm.module.player_state.network.ClientSendRequestState;

@ClientModule(module = PlayerState.class)
public class PlayerStateClient extends CharmModule {
    public static ClientSendRequestState CLIENT_SEND_REQUEST_STATE;
    public static ClientReceiveState CLIENT_RECEIVE_STATE;

    @Override
    public void runWhenEnabled() {
        CLIENT_SEND_REQUEST_STATE = new ClientSendRequestState();
        CLIENT_RECEIVE_STATE = new ClientReceiveState();

        ClientTickEvents.START_CLIENT_TICK.register(this::handleClientTick);
    }

    private void handleClientTick(Minecraft client) {
        if (client.player != null && client.player.level.getGameTime() % PlayerState.heartbeat == 0) {
            CLIENT_SEND_REQUEST_STATE.send();
        }
    }
}
