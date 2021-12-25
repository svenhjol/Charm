package svenhjol.charm.module.atlases.network;

import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerSender;

@Id("strange:swapped_atlas_slot")
public class ServerSendSwappedSlot extends ServerSender {
    public void send(ServerPlayer player, int slot) {
        super.send(player, buf -> buf.writeInt(slot));
    }
}
