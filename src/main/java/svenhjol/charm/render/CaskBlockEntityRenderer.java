package svenhjol.charm.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;
import svenhjol.charm.blockentity.CaskBlockEntity;

@Environment(EnvType.CLIENT)
public class CaskBlockEntityRenderer<T extends CaskBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererFactory.Context context;

    public CaskBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public boolean rendersOutsideBoundingBox(T blockEntity) {
        return true;
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity == null)
            return;

        if (entity.name == null || entity.name.isEmpty())
            return;

        final MinecraftClient client = MinecraftClient.getInstance();
        ClientPlayerEntity player = client.player;
        if (player == null)
            return;

        Direction facing = player.getHorizontalFacing();
        LiteralText text = new LiteralText(entity.name);

        int x = entity.getPos().getX();
        int y = entity.getPos().getY();
        int z = entity.getPos().getZ();

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

        BlockEntityRenderDispatcher dispatcher = this.context.getRenderDispatcher();
        Camera camera = dispatcher.camera;
        double d = camera.getPos().squaredDistanceTo(x, y, z);

        if (d < 32) {
            matrices.push();
            matrices.translate(0.5F + xo, 0.85F, 0.5F + zo);
            matrices.multiply(camera.getRotation());
            matrices.scale(-0.014F, -0.014F, 0.014F);
            Matrix4f matrix4f = matrices.peek().getModel();
            float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
            int j = (int)(g * 255.0F) << 24;
            TextRenderer textRenderer = client.textRenderer;
            float h = (float)(-textRenderer.getWidth(text) / 2);
            textRenderer.draw(text, h, 0, 0xFFFFFF, false, matrix4f, vertexConsumers, false, j, 255);
            matrices.pop();
        }
    }
}
