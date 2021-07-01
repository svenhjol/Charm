package svenhjol.charm.module.storage_labels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.ClientModule;

import java.util.List;
import java.util.Optional;

@svenhjol.charm.annotation.ClientModule(module = StorageLabels.class)
public class StorageLabelsClient extends ClientModule {
    public static final ThreadLocal<BlockEntityRendererProvider.Context> chestBlockEntityContext = new ThreadLocal<>();

    @Override
    public void run() {
        ClientPlayNetworking.registerGlobalReceiver(StorageLabels.MSG_CLIENT_UPDATE_CUSTOM_NAME, this::handleUpdateCustomName);
        ClientPlayNetworking.registerGlobalReceiver(StorageLabels.MSG_CLIENT_HAS_NO_CUSTOM_NAME, this::handleHasNoCustomName);
        ClientPlayNetworking.registerGlobalReceiver(StorageLabels.MSG_CLIENT_CLEAR_CUSTOM_NAME, this::handleClearCustomName);

        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityType.BARREL, LootableContainerBlockEntityRenderer::new);
    }

    private void handleUpdateCustomName(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.of(data.readLong());
        String label = data.readUtf();

        client.execute(() ->
            ClientHelper.getWorld()
                .ifPresent(world -> getLootableContainerBlockEntity(world, pos)
                    .ifPresent(container -> container.setCustomName(new TextComponent(label)))));
    }

    private void handleHasNoCustomName(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.of(data.readLong());

        client.execute(() ->
            ClientHelper.getWorld()
                .ifPresent(world -> LootableContainerBlockEntityRenderer.cachedPos.put(pos, (long) -1)));
    }

    private void handleClearCustomName(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.of(data.readLong());

        client.execute(() ->
            ClientHelper.getWorld()
                .ifPresent(world -> LootableContainerBlockEntityRenderer.cachedPos.remove(pos)));
    }

    private Optional<RandomizableContainerBlockEntity> getLootableContainerBlockEntity(Level world, BlockPos pos) {
        return Optional.ofNullable((RandomizableContainerBlockEntity)world.getBlockEntity(pos));
    }

    public static void renderLabel(PoseStack matrices, MultiBufferSource vertexConsumers, Player player, Camera camera, List<Component> text) {
        if (!Charm.LOADER.isEnabled(StorageLabels.class))
            return;

        if (!StorageLabels.alwaysShow && !player.isShiftKeyDown())
            return;

        Optional<Font> optTextRenderer = ClientHelper.getTextRenderer();
        Optional<Options> optGameOptions = ClientHelper.getGameOptions();

        if (!optTextRenderer.isPresent() || !optGameOptions.isPresent())
            return;

        Font textRenderer = optTextRenderer.get();
        Options gameOptions = optGameOptions.get();

        float xo = 0.0F;
        float zo = 0.0F;

        switch (player.getDirection()) {
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

        matrices.pushPose();
        matrices.translate(0.5F + xo, 0.75F, 0.5F + zo);
        matrices.mulPose(camera.rotation());
        matrices.scale(-StorageLabels.SCALE, -StorageLabels.SCALE, StorageLabels.SCALE);
        Matrix4f matrix4f = matrices.last().pose();
        float g = gameOptions.getBackgroundOpacity(0.0F);
        int j = (int)(g * 255.0F) << 24;

        for (int i = 0; i < text.size(); i++) {
            Component t = text.get(i);
            float h = (float)(-textRenderer.width(t) / 2);
            textRenderer.drawInBatch(t, h, i * 10, 0xFFFFFF, false, matrix4f, vertexConsumers, false, j, 255);
        }

        matrices.popPose();
    }
}
