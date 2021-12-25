package svenhjol.charm.module.atlases.network;

import svenhjol.charm.network.ClientSender;
import svenhjol.charm.network.Id;

@Id("strange:swap_atlas")
public class ClientSendSwapAtlas extends ClientSender {
    public void send(int slot) {
        super.send(buf -> buf.writeInt(slot));
    }
}
