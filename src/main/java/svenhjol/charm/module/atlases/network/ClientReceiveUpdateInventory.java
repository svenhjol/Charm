package svenhjol.charm.module.atlases.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.module.atlases.AtlasesClient;
import svenhjol.charm.network.ClientReceiver;
import svenhjol.charm.network.Id;

@Id("strange:update_atlas_inventory")
public class ClientReceiveUpdateInventory extends ClientReceiver {
    @Override
    public void handle(Minecraft client, FriendlyByteBuf buffer) {
        int atlasSlot = buffer.readInt();
        client.execute(() -> AtlasesClient.updateInventory(atlasSlot));
    }

    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
