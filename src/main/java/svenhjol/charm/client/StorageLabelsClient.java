package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.helper.ClientHelper;
import svenhjol.charm.module.StorageLabels;
import svenhjol.charm.render.LootableContainerBlockEntityRenderer;

import java.util.Optional;

public class StorageLabelsClient extends CharmClientModule {
    public StorageLabelsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        ClientPlayNetworking.registerGlobalReceiver(StorageLabels.MSG_CLIENT_UPDATE_CUSTOM_NAME, this::handleUpdateCustomName);
        ClientPlayNetworking.registerGlobalReceiver(StorageLabels.MSG_CLIENT_HAS_NO_CUSTOM_NAME, this::handleHasNoCustomName);
        ClientPlayNetworking.registerGlobalReceiver(StorageLabels.MSG_CLIENT_CLEAR_CUSTOM_NAME, this::handleClearCustomName);

        BlockEntityRendererRegistry.INSTANCE.register(BlockEntityType.BARREL, LootableContainerBlockEntityRenderer::new);
    }

    private void handleUpdateCustomName(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());
        String label = data.readString();

        client.execute(() ->
            ClientHelper.getWorld()
                .ifPresent(world -> getLootableContainerBlockEntity(world, pos)
                    .ifPresent(container -> container.setCustomName(new LiteralText(label)))));
    }

    private void handleHasNoCustomName(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());

        client.execute(() ->
            ClientHelper.getWorld()
                .ifPresent(world -> LootableContainerBlockEntityRenderer.cachedPos.put(pos, (long) -1)));
    }

    private void handleClearCustomName(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());

        client.execute(() ->
            ClientHelper.getWorld()
                .ifPresent(world -> LootableContainerBlockEntityRenderer.cachedPos.remove(pos)));
    }

    private Optional<LootableContainerBlockEntity> getLootableContainerBlockEntity(World world, BlockPos pos) {
        return Optional.ofNullable((LootableContainerBlockEntity)world.getBlockEntity(pos));
    }

    public static void renderLabel(MatrixStack matrices, VertexConsumerProvider vertexConsumers, PlayerEntity player, Camera camera, Text text) {
        if (!ModuleHandler.enabled(StorageLabels.class))
            return;

        if (!StorageLabels.alwaysShow && !player.isSneaking())
            return;

        Optional<TextRenderer> optTextRenderer = ClientHelper.getTextRenderer();
        Optional<GameOptions> optGameOptions = ClientHelper.getGameOptions();

        if (!optTextRenderer.isPresent() || !optGameOptions.isPresent())
            return;

        TextRenderer textRenderer = optTextRenderer.get();
        GameOptions gameOptions = optGameOptions.get();

        float xo = 0.0F;
        float zo = 0.0F;

        switch (player.getHorizontalFacing()) {
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

        matrices.push();
        matrices.translate(0.5F + xo, 0.85F, 0.5F + zo);
        matrices.multiply(camera.getRotation());
        matrices.scale(-0.014F, -0.014F, 0.014F);
        Matrix4f matrix4f = matrices.peek().getModel();
        float g = gameOptions.getTextBackgroundOpacity(0.0F);
        int j = (int)(g * 255.0F) << 24;
        float h = (float)(-textRenderer.getWidth(text) / 2);
        textRenderer.draw(text, h, 0, 0xFFFFFF, false, matrix4f, vertexConsumers, false, j, 255);
        matrices.pop();
    }
}
