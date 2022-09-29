package svenhjol.charm.module.atlases.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.module.atlases.AtlasesClient;
import svenhjol.charm.network.ClientReceiver;
import svenhjol.charm.network.Id;

@Id("charm:swapped_atlas_slot")
public class ClientReceiveSwappedSlot extends ClientReceiver {
    @Override
    public void handle(Minecraft client, FriendlyByteBuf buffer) {
        int swappedSlot = buffer.readInt();
        client.execute(() -> AtlasesClient.swappedSlot = swappedSlot);
    }
}
