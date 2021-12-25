package svenhjol.charm.module.player_state.network;

import svenhjol.charm.network.ClientSender;
import svenhjol.charm.network.Id;

/**
 * Client sends empty request for player state to be sent.
 */
@Id("charm:request_player_state")
public class ClientSendRequestState extends ClientSender {
    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
