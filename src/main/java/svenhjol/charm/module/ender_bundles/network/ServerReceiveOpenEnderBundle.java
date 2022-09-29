package svenhjol.charm.module.ender_bundles.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerReceiver;
import svenhjol.charm.module.ender_bundles.EnderBundles;

@Id("charm:open_ender_bundle")
public class ServerReceiveOpenEnderBundle extends ServerReceiver {
    @Override
    public void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buf) {
        server.execute(() -> EnderBundles.handleReceiveOpenEnderBundle(player));
    }
}