package svenhjol.charm.base.message;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.iface.IMesonMessage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClientUpdatePlayerState implements IMesonMessage {
    private final CompoundNBT input;
    public static List<Consumer<CompoundNBT>> runOnUpdate = new ArrayList<>();

    public ClientUpdatePlayerState(CompoundNBT input) {
        this.input = input;
    }

    public static void encode(ClientUpdatePlayerState msg, PacketBuffer buf) {
        String serialized = "";

        try {
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            CompressedStreamTools.writeCompressed(msg.input, out);
            serialized = Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (Exception e) {
            Charm.LOG.warn("Failed to compress structures");
        }
        buf.writeString(serialized);
    }

    public static ClientUpdatePlayerState decode(PacketBuffer buf) {
        CompoundNBT input = new CompoundNBT();

        try {
            final byte[] byteData = Base64.getDecoder().decode(buf.readString());
            input = CompressedStreamTools.readCompressed(new ByteArrayInputStream(byteData));
        } catch (Exception e) {
            Charm.LOG.warn("Failed to uncompress structures");
        }

        return new ClientUpdatePlayerState(input);
    }

    public static class Handler {
        public static void handle(final ClientUpdatePlayerState msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                Charm.client.updateFromServer(msg.input);

                // update subscribed clients
                runOnUpdate.forEach(action -> action.accept(msg.input));
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
