package svenhjol.charm.module.clear_item_frames.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerSender;

@Id("charm:remove_amethyst_from_item_frame")
public class ServerSendRemoveAmethyst extends ServerSender {
    public void send(ServerPlayer player, BlockPos pos) {
        super.send(player, buf -> buf.writeBlockPos(pos));
    }
}
