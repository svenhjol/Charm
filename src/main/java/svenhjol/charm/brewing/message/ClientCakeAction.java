package svenhjol.charm.brewing.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import svenhjol.charm.brewing.module.FlavoredCake;
import svenhjol.meson.iface.IMesonMessage;

import java.util.function.Supplier;

public class ClientCakeAction implements IMesonMessage {
    private BlockPos pos;

    public ClientCakeAction(BlockPos pos) {
        this.pos = pos;
    }

    public static void encode(ClientCakeAction msg, PacketBuffer buf) {
        buf.writeLong(msg.pos.toLong());
    }

    public static ClientCakeAction decode(PacketBuffer buf) {
        return new ClientCakeAction(BlockPos.fromLong(buf.readLong()));
    }

    public static class Handler {
        public static void handle(final ClientCakeAction msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> FlavoredCake.effectImbue(msg.pos));
            ctx.get().setPacketHandled(true);
        }
    }
}
