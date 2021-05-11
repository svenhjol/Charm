package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.helper.ClientHelper;
import svenhjol.charm.module.StorageLabels;

import java.util.Optional;

public class StorageLabelsClient extends CharmClientModule {
    public StorageLabelsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        ClientPlayNetworking.registerGlobalReceiver(StorageLabels.MSG_CLIENT_UPDATE_CUSTOM_NAME, this::handleUpdateCustomName);
    }

    private void handleUpdateCustomName(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());
        String label = data.readString();

        client.execute(() -> {
            ClientHelper.getWorld().ifPresent(world -> {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof LootableContainerBlockEntity) {
                    LootableContainerBlockEntity container = (LootableContainerBlockEntity) blockEntity;
                    container.setCustomName(new LiteralText(label));
                }
            });
        });
    }

    public static void renderLabel(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Camera camera, Direction facing, Text text) {
        if (!ModuleHandler.enabled(StorageLabels.class))
            return;

        Optional<TextRenderer> optTextRenderer = ClientHelper.getTextRenderer();
        Optional<GameOptions> optGameOptions = ClientHelper.getGameOptions();

        if (!optTextRenderer.isPresent() || !optGameOptions.isPresent())
            return;

        TextRenderer textRenderer = optTextRenderer.get();
        GameOptions gameOptions = optGameOptions.get();

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

        matrices.push();
        matrices.translate(0.5F + xo, 0.85F, 0.5F + zo);
        matrices.multiply(camera.getRotation());
        matrices.scale(-0.014F, -0.014F, 0.014F);
        Matrix4f matrix4f = matrices.peek().getModel();
        float g = gameOptions.getTextBackgroundOpacity(0.25F);
        int j = (int)(g * 255.0F) << 24;
        float h = (float)(-textRenderer.getWidth(text) / 2);
        textRenderer.draw(text, h, 0, 0xFFFFFF, false, matrix4f, vertexConsumers, false, j, 255);
        matrices.pop();
    }
}
