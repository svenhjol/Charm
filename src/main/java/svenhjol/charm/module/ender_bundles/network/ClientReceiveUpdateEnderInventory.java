package svenhjol.charm.module.ender_bundles.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import svenhjol.charm.network.ClientReceiver;
import svenhjol.charm.network.Id;
import svenhjol.charm.module.ender_bundles.EnderBundlesClient;

@Id("charm:update_ender_inventory")
public class ClientReceiveUpdateEnderInventory extends ClientReceiver {
    @Override
    public void handle(Minecraft client, FriendlyByteBuf buf) {
        var tag = getCompoundTag(buf).orElseThrow();
        client.execute(() -> EnderBundlesClient.handleReceiveUpdateEnderInventory(client.player, tag));
    }
}