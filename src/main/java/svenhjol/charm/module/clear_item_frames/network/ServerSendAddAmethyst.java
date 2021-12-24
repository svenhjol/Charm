package svenhjol.charm.module.clear_item_frames.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerSender;

@Id("charm:add_amethyst_to_item_frame")
public class ServerSendAddAmethyst extends ServerSender {
    public void send(ServerPlayer player, BlockPos pos) {
        super.send(player, buf -> buf.writeBlockPos(pos));
    }
}
