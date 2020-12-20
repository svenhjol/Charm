package svenhjol.charm.client;

import net.minecraft.block.ShulkerBoxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapState;
import net.minecraft.text.OrderedText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.world.World;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.ItemHelper;
import svenhjol.charm.event.RenderTooltipCallback;

import java.util.List;

public class MapTooltipsClient extends CharmClientModule {
    private static final RenderLayer MAP_BACKGROUND = RenderLayer.getText(new Identifier("textures/map/map_background.png"));

    public MapTooltipsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void init() {
        RenderTooltipCallback.EVENT.register(this::handleRenderTooltip);
    }

    private ActionResult handleRenderTooltip(MatrixStack matrices, ItemStack stack, List<? extends OrderedText> lines, int x, int y) {
        if (stack != null && stack.getItem() == Items.FILLED_MAP) {
            boolean result = renderTooltip(matrices, stack, lines, x, y);
            if (result)
                return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    private boolean renderTooltip(MatrixStack matrices, ItemStack stack, List<? extends OrderedText> lines, int tx, int ty) {
        if (stack.getItem() != Items.FILLED_MAP) return false;

        final MinecraftClient mc = MinecraftClient.getInstance();
        final World world = mc.world;
        if (world == null) return false;

        MapState data = FilledMapItem.getMapState(stack, world);

        if (data == null) return false;

        int x = tx;
        int y = ty - 72;
        int w = 64;
        int right = x + w;

        if (right > mc.getWindow().getScaledWidth())
            x = mc.getWindow().getScaledWidth() - w;

        if (y < 0)
            y = ty + lines.size() * 10 + 8;

        int light = 240;

        matrices.push();
        matrices.translate(x, y, 500.0);
        matrices.scale(0.5F, 0.5F, 1.0F);
        VertexConsumerProvider.Immediate bufferSource = mc.getBufferBuilders().getEntityVertexConsumers();
        final VertexConsumer builder = bufferSource.getBuffer(MAP_BACKGROUND);
        Matrix4f matrix4f = matrices.peek().getModel();
        builder.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(light).next();
        builder.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(light).next();
        builder.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(light).next();
        builder.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(light).next();
        matrices.push();
        matrices.translate(0.0, 0.0, 1.0);
        mc.gameRenderer.getMapRenderer().draw(matrices, bufferSource, data, false, light);
        matrices.pop();
        matrices.pop();
        return true;
    }
}

