package svenhjol.charm.module.storage_labels;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import svenhjol.charm.helper.ClientHelper;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class LootableContainerBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private static final int REFRESH_NAME_TICKS = 60;
    private final BlockEntityRendererFactory.Context context;
    public static Map<BlockPos, Long> cachedPos = new WeakHashMap<>();

    public LootableContainerBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public boolean rendersOutsideBoundingBox(T blockEntity) {
        return true;
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        render(context.getRenderDispatcher(), entity, tickDelta, matrices, vertexConsumers, light, overlay);
    }

    public static <T extends BlockEntity> void render(BlockEntityRenderDispatcher dispatcher, T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!(entity instanceof LootableContainerBlockEntity))
            return;

        LootableContainerBlockEntity container = (LootableContainerBlockEntity) entity;
        BlockPos pos = container.getPos();
        World world = entity.getWorld();

        if (world == null)
            return;

        if (entity instanceof BarrelBlockEntity && !StorageLabels.showBarrelLabels)
            return;

        if (entity instanceof ChestBlockEntity && !StorageLabels.showChestLabels)
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
        if (distance < StorageLabels.viewDistance)
            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, Collections.singletonList(text));
    }
}
