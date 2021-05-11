package svenhjol.charm.module;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.StorageLabelsClient;

import javax.annotation.Nullable;

@Module(mod = Charm.MOD_ID, client = StorageLabelsClient.class, description = "Shows the custom name of a storage block as a floating label when the player is sneaking.")
public class StorageLabels extends CharmModule {
    public static final Identifier MSG_SERVER_QUERY_CUSTOM_NAME = new Identifier(Charm.MOD_ID, "server_query_custom_name");
    public static final Identifier MSG_CLIENT_UPDATE_CUSTOM_NAME = new Identifier(Charm.MOD_ID, "client_update_custom_name");
    public static final Identifier MSG_CLIENT_HAS_NO_CUSTOM_NAME = new Identifier(Charm.MOD_ID, "client_has_no_custom_name");
    public static final Identifier MSG_CLIENT_CLEAR_CUSTOM_NAME = new Identifier(Charm.MOD_ID, "client_clear_custom_name");

    @Config(name = "Always show", description = "If true, floating labels will show even if the player is not sneaking.")
    public static boolean alwaysShow = false;

    @Override
    public void init() {
        ServerPlayNetworking.registerGlobalReceiver(MSG_SERVER_QUERY_CUSTOM_NAME, this::handleQueryCustomName);
        PlayerBlockBreakEvents.AFTER.register(this::handleBlockBreak);
    }

    private void handleBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
        if (!world.isClient && blockEntity instanceof LootableContainerBlockEntity) {
            PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
            packet.writeLong(pos.asLong());
            ServerPlayNetworking.send((ServerPlayerEntity) player, MSG_CLIENT_CLEAR_CUSTOM_NAME, packet);
        }
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

                PacketByteBuf packet = new PacketByteBuf(Unpooled.buffer());
                packet.writeLong(pos.asLong());

                if (container.hasCustomName()) {
                    packet.writeString(container.getDisplayName().asString());
                    ServerPlayNetworking.send(player, MSG_CLIENT_UPDATE_CUSTOM_NAME, packet);
                } else {
                    ServerPlayNetworking.send(player, MSG_CLIENT_HAS_NO_CUSTOM_NAME, packet);
                }
            }
        });
    }
}
