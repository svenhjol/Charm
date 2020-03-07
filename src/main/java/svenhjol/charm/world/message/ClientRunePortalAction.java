package svenhjol.charm.world.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import svenhjol.charm.world.module.EndPortalRunes;
import svenhjol.meson.iface.IMesonMessage;

import java.util.function.Supplier;

public class ClientRunePortalAction implements IMesonMessage {
    public static final int LINKED = 0;
    public static final int UNLINKED = 1;
    public static final int TRAVELLED = 2;

    private BlockPos pos;
    private int action;

    public ClientRunePortalAction(int action, BlockPos pos) {
        this.action = action;
        this.pos = pos;
    }

    public static void encode(ClientRunePortalAction msg, PacketBuffer buf) {
        buf.writeInt(msg.action);
        buf.writeLong(msg.pos.toLong());
    }

    public static ClientRunePortalAction decode(PacketBuffer buf) {
        int action = buf.readInt();
        BlockPos pos = BlockPos.fromLong(buf.readLong());

        return new ClientRunePortalAction(action, pos);
    }

    public static class Handler {
        public static void handle(final ClientRunePortalAction msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                switch (msg.action) {
                    case LINKED:
                        EndPortalRunes.effectPortalLinked(msg.pos);
                        break;

                    case UNLINKED:
                        EndPortalRunes.effectPortalUnlinked(msg.pos);
                        break;

                    case TRAVELLED:
                        EndPortalRunes.effectPortalTravelled(msg.pos);
                        break;
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
