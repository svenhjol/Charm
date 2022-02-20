package svenhjol.charm.module.lava_bucket_destroys_items.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import svenhjol.charm.module.lava_bucket_destroys_items.LavaBucketDestroysItems;
import svenhjol.charm.network.Id;
import svenhjol.charm.network.ServerReceiver;

@Id("charm:destroy_with_lava_bucket")
public class ServerReceiveDestroyWithLavaBucket extends ServerReceiver {
    @Override
    public void handle(MinecraftServer server, ServerPlayer player, FriendlyByteBuf buffer) {
        server.execute(() -> LavaBucketDestroysItems.triggerDestroyedItem(player));
    }
}
