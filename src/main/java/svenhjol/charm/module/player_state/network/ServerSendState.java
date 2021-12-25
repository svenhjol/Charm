package svenhjol.charm.module.player_state.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerSender;

@Id("charm:player_state")
public class ServerSendState extends ServerSender {
    public void send(ServerPlayer player, CompoundTag tag) {
        super.send(player, buf -> buf.writeNbt(tag));
    }

    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
