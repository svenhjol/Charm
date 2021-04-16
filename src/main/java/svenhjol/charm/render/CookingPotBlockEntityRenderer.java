package svenhjol.charm.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.blockentity.CookingPotBlockEntity;
import svenhjol.charm.client.CookingPotsClient;

import java.util.List;

public class CookingPotBlockEntityRenderer<T extends CookingPotBlockEntity> implements BlockEntityRenderer<T> {
    private int index = 0;
    private int ticks = 0;

    public CookingPotBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        // allows client new
    }

    @Override
    public boolean rendersOutsideBoundingBox(T blockEntity) {
        return true;
    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        World world = entity.getWorld();
        if (world == null)
            return;

        List<Item> items = CookingPotsClient.getResolvedItems(entity.contents);
        if (items.isEmpty())
            return;

        matrices.push();
        matrices.scale(0.57F, 0.57F, 0.57F);
        matrices.translate(0.82F, 0.68F, 0.82F);

        if (ticks > 360) {
            ticks = 0;
            if (++index > entity.contents.size() - 1)
                index = 0;
        }

        ItemStack stack;
        try {
             stack = new ItemStack(items.get(index));
        } catch (IndexOutOfBoundsException e) {
            Charm.LOG.warn("THIS AGAIN, WHY IS IT ALWAYS THIS");
            index = 0;
            matrices.pop();
            return;
        }

        float l = MathHelper.sin(ticks / 3.1415927F) / (4.0F + ticks / 3.0F);
        matrices.translate(0, 0.52 + (((ticks > 180 ? (360 - ticks) : ticks) / 180.0F) * 0.39F), 0);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float)ticks));

        ticks++;

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.hashCode());
        matrices.pop();
    }
}
