package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.StorageLabelsClient;

@Module(mod = Charm.MOD_ID, client = StorageLabelsClient.class)
public class StorageLabels extends CharmModule {
    public static final Identifier MSG_SERVER_QUERY_CUSTOM_NAME = new Identifier(Charm.MOD_ID, "server_query_custom_name");
    public static final Identifier MSG_CLIENT_UPDATE_CUSTOM_NAME = new Identifier(Charm.MOD_ID, "client_update_custom_name");

    @Override
    public void init() {
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_QUERY_CUSTOM_NAME, this::handleQueryCustomName);
    }

    private void handleQueryCustomName(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());
        server.execute(() -> {
            ServerWorld world = (ServerWorld) player.world;
            if (world == null)
                return;

            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof LootableContainerBlockEntity) {
                LootableContainerBlockEntity container = (LootableContainerBlockEntity) blockEntity;
                if (container.hasCustomName()) {
                    PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                    packet.writeLong(pos.asLong());
                    packet.writeString(container.getDisplayName().asString());
                    ServerPlayNetworking.send(player, MSG_CLIENT_UPDATE_CUSTOM_NAME, packet);
                }
            }
        });
    }
}
