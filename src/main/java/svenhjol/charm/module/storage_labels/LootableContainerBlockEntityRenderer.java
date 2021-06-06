package svenhjol.charm.module.storage_labels;

import com.mojang.blaze3d.vertex.PoseStack;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.storage_labels.StorageLabels;
import svenhjol.charm.module.storage_labels.StorageLabelsClient;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class LootableContainerBlockEntityRenderer<T extends BlockEntity> implements BlockEntityRenderer<T> {
    private static final int REFRESH_NAME_TICKS = 60;
    private final BlockEntityRendererProvider.Context context;
    public static Map<BlockPos, Long> cachedPos = new WeakHashMap<>();

    public LootableContainerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return true;
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        render(context.getBlockEntityRenderDispatcher(), entity, tickDelta, matrices, vertexConsumers, light, overlay);
    }

    public static <T extends BlockEntity> void render(BlockEntityRenderDispatcher dispatcher, T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (!(entity instanceof RandomizableContainerBlockEntity))
            return;

        RandomizableContainerBlockEntity container = (RandomizableContainerBlockEntity) entity;
        BlockPos pos = container.getBlockPos();
        Level world = entity.getLevel();

        if (world == null)
            return;

        if (entity instanceof BarrelBlockEntity && !svenhjol.charm.module.storage_labels.StorageLabels.showBarrelLabels)
            return;

        if (entity instanceof ChestBlockEntity && !svenhjol.charm.module.storage_labels.StorageLabels.showChestLabels)
            return;

        if (!container.hasCustomName()) {
            // ask the server to update this container with a custom name
            if (!cachedPos.containsKey(pos)) {
                cachedPos.put(pos, world.getGameTime());
            } else if (cachedPos.get(pos) == -1) {
                // server updated this to confirm there's no custom name here, don't do anything
                return;
            } else if (world.getGameTime() - cachedPos.get(pos) > REFRESH_NAME_TICKS) {
                FriendlyByteBuf data = new FriendlyByteBuf(Unpooled.buffer());
                data.writeLong(pos.asLong());
                ClientPlayNetworking.send(svenhjol.charm.module.storage_labels.StorageLabels.MSG_SERVER_QUERY_CUSTOM_NAME, data);
                cachedPos.remove(pos);
            }
            return;
        }

        cachedPos.remove(pos);

        final Minecraft client = Minecraft.getInstance();
        LocalPlayer player = client.player;
        if (player == null)
            return;

        Camera camera = dispatcher.camera;
        TextComponent text = (TextComponent) container.getDisplayName();

        double distance = ClientHelper.getBlockEntityDistance(player, container, camera);
        if (distance < StorageLabels.viewDistance)
            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, Collections.singletonList(text));
    }
}
