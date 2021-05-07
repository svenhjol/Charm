package svenhjol.charm.render;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;
import svenhjol.charm.module.StorageLabels;

import java.util.Map;
import java.util.WeakHashMap;

public class LootableContainerLabelRenderer {
    public static Map<BlockPos, Long> cachedPosQuery = new WeakHashMap<>();

    public static <T extends BlockEntity> void render(BlockEntityRenderDispatcher dispatcher, T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!(entity instanceof LootableContainerBlockEntity))
            return;

        LootableContainerBlockEntity container = (LootableContainerBlockEntity) entity;
        BlockPos pos = container.getPos();
        World world = entity.getWorld();

        if (world == null)
            return;

        if (!container.hasCustomName()) {
            // ask the server to update this container with a custon name
            if (!cachedPosQuery.containsKey(pos)) {
                cachedPosQuery.put(pos, world.getTime());
            } else if (world.getTime() - cachedPosQuery.get(pos) > 60) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                data.writeLong(pos.asLong());
                ClientPlayNetworking.send(StorageLabels.MSG_SERVER_QUERY_CUSTOM_NAME, data);
                cachedPosQuery.remove(pos);
            }
            return;
        }

        cachedPosQuery.remove(pos);

        final MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null)
            return;

        Direction facing = player.getHorizontalFacing();
        LiteralText text = (LiteralText) container.getDisplayName();

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        float xo = 0.0F;
        float zo = 0.0F;

        switch (facing) {
            case EAST:
                xo -= 0.65F;
                break;

            case WEST:
                xo += 0.65F;
                break;

            case SOUTH:
                zo -= 0.65F;
                break;

            case NORTH:
                zo += 0.65F;
                break;
        }

        Camera camera = dispatcher.camera;
        double d = camera.getPos().squaredDistanceTo(x, y, z);

        if (d < 32) {
            matrices.push();
            matrices.translate(0.5F + xo, 0.85F, 0.5F + zo);
            matrices.multiply(camera.getRotation());
            matrices.scale(-0.014F, -0.014F, 0.014F);
            Matrix4f matrix4f = matrices.peek().getModel();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = 0;
            TextRenderer textRenderer = client.textRenderer;
            float h = (float)(-textRenderer.getWidth(text) / 2);
            textRenderer.draw(text, h, 0, 0xFFFFFF, false, matrix4f, vertexConsumers, false, j, 255);
            matrices.pop();
        }
    }
}
