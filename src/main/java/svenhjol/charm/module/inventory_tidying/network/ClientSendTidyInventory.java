package svenhjol.charm.module.inventory_tidying.network;

import svenhjol.charm.network.ClientSender;
import svenhjol.charm.network.Id;

@Id("charm:tidy_inventory")
public class ClientSendTidyInventory extends ClientSender {
    public void send(int type) {
        super.send(buf -> buf.writeInt(type));
    }
}
