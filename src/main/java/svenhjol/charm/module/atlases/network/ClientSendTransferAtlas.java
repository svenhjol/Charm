package svenhjol.charm.module.atlases.network;

import svenhjol.charm.module.atlases.Atlases;
import svenhjol.charm.network.ClientSender;
import svenhjol.charm.network.Id;

@Id("charm:transfer_atlas")
public class ClientSendTransferAtlas extends ClientSender {
    public void send(int atlasSlot, int mapX, int mapZ, Atlases.MoveMode mode) {
        super.send(buf -> {
            buf.writeInt(atlasSlot);
            buf.writeInt(mapX);
            buf.writeInt(mapZ);
            buf.writeEnum(mode);
        });
    }
}
