package svenhjol.charm.module.player_state.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.api.event.ClientStateUpdateCallback;
import svenhjol.charm.network.ClientReceiver;
import svenhjol.charm.network.Id;

/**
 * When client receives packet from server, fire event for client listeners.
 */
@Id("charm:player_state")
public class ClientReceiveState extends ClientReceiver {
    @Override
    public void handle(Minecraft client, FriendlyByteBuf buffer) {
        var tag = getCompoundTag(buffer).orElseThrow();

        client.execute(() -> ClientStateUpdateCallback.EVENT.invoker().interact(tag));
    }

    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
