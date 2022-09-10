package svenhjol.charm.module.inventory_tidying.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.module.inventory_tidying.InventoryTidying;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerReceiver;

@Id("charm:tidy_inventory")
public class ServerReceiveTidyInventory extends ServerReceiver {
    @Override
    public void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buffer) {
        int type = buffer.readInt();
        server.execute(() -> InventoryTidying.serverCallback(player, type));
    }

    @Override
    protected boolean showDebugMessages() {
        return false;
    }
}
