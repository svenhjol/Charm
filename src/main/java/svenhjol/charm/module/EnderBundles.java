package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.EnderBundlesClient;
import svenhjol.charm.item.EnderBundleItem;

@Module(mod = Charm.MOD_ID, client = EnderBundlesClient.class, description = "EnderBundles")
public class EnderBundles extends CharmModule {
    public static final Identifier MSG_SERVER_UPDATE_ENDER_INVENTORY = new Identifier(Charm.MOD_ID, "server_update_ender_inventory");
    public static final Identifier MSG_CLIENT_UPDATE_ENDER_INVENTORY = new Identifier(Charm.MOD_ID, "server_client_ender_inventory");

    public static EnderBundleItem ENDER_BUNDLE;

    @Override
    public void register() {
        ENDER_BUNDLE = new EnderBundleItem(this);

        // register server message handler to call the serverCallback
        ServerSidePacketRegistry.INSTANCE.register(MSG_SERVER_UPDATE_ENDER_INVENTORY, (context, data) -> {
            context.getTaskQueue().execute(() -> {
                ServerPlayerEntity player = (ServerPlayerEntity)context.getPlayer();
                if (player == null)
                    return;

                NbtCompound tag = new NbtCompound();
                tag.put("EnderItems", player.getEnderChestInventory().getTags());

                PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
                buffer.writeCompound(tag);
                ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, MSG_CLIENT_UPDATE_ENDER_INVENTORY, buffer);
            });
        });
    }
}
