package svenhjol.charm.render;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.base.helper.ClientHelper;
import svenhjol.charm.client.StorageLabelsClient;
import svenhjol.charm.module.StorageLabels;

import java.util.Map;
import java.util.WeakHashMap;

public class LootableContainerLabelRenderer {
    private static final int REFRESH_NAME_TICKS = 60;
    public static Map<BlockPos, Long> cachedPos = new WeakHashMap<>();

    public static <T extends BlockEntity> void render(BlockEntityRenderDispatcher dispatcher, T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!(entity instanceof LootableContainerBlockEntity))
            return;

        LootableContainerBlockEntity container = (LootableContainerBlockEntity) entity;
        BlockPos pos = container.getPos();
        World world = entity.getWorld();

        if (world == null)
            return;

        if (!container.hasCustomName()) {
            // ask the server to update this container with a custom name
            if (!cachedPos.containsKey(pos)) {
                cachedPos.put(pos, world.getTime());
            } else if (cachedPos.get(pos) == -1) {
                // server updated this to confirm there's no custom name here, don't do anything
                return;
            } else if (world.getTime() - cachedPos.get(pos) > REFRESH_NAME_TICKS) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeLong(pos.asLong());
                ClientPlayNetworking.send(StorageLabels.MSG_SERVER_QUERY_CUSTOM_NAME, data);
                cachedPos.remove(pos);
            }
            return;
        }

        cachedPos.remove(pos);

        final MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null)
            return;

        Camera camera = dispatcher.camera;
        LiteralText text = (LiteralText) container.getDisplayName();

        double distance = ClientHelper.getBlockEntityDistance(player, container, camera);
        if (distance < 32)
            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, text);
    }
}
