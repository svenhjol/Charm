package svenhjol.charm.module.hover_sorting.network;

import svenhjol.charm.network.ClientSender;
import svenhjol.charm.network.Id;

@Id("charm:scrolled_on_hover")
public class ClientSendScrolledOnHover extends ClientSender {
    public void send(int slotIndex, boolean direction) {
        super.send(buf -> {
            buf.writeInt(slotIndex);
            buf.writeBoolean(direction);
        });
    }

    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
