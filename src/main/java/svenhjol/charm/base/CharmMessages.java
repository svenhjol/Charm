package svenhjol.charm.base;

import svenhjol.charm.base.message.ClientUpdatePlayerState;
import svenhjol.charm.base.message.ServerUpdatePlayerState;
import svenhjol.charm.brewing.message.ClientCakeAction;
import svenhjol.charm.tools.message.ClientGlowingAction;
import svenhjol.charm.world.message.ClientRunePortalAction;
import svenhjol.meson.MesonInstance;
import svenhjol.meson.handler.PacketHandler;

public class CharmMessages {
    public static void init(MesonInstance instance) {
        PacketHandler handler = instance.getPacketHandler();

        handler.register(ClientCakeAction.class, ClientCakeAction::encode, ClientCakeAction::decode, ClientCakeAction.Handler::handle);
        handler.register(ClientGlowingAction.class, ClientGlowingAction::encode, ClientGlowingAction::decode, ClientGlowingAction.Handler::handle);
        handler.register(ClientRunePortalAction.class, ClientRunePortalAction::encode, ClientRunePortalAction::decode, ClientRunePortalAction.Handler::handle);
        handler.register(ClientUpdatePlayerState.class, ClientUpdatePlayerState::encode, ClientUpdatePlayerState::decode, ClientUpdatePlayerState.Handler::handle);
        handler.register(ServerUpdatePlayerState.class, ServerUpdatePlayerState::encode, ServerUpdatePlayerState::decode, ServerUpdatePlayerState.Handler::handle);
    }
}
