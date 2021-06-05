package svenhjol.charm.module.cooking_pots;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.storage_labels.StorageLabels;
import svenhjol.charm.module.storage_labels.StorageLabelsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CookingPotBlockEntityRenderer<T extends CookingPotBlockEntity> implements BlockEntityRenderer<T> {
    private BlockEntityRendererFactory.Context context;
    private int index = 0;
    private float ticks = 0.0F;

    public CookingPotBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
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

        if (!CookingPots.showLabel)
            return;

        List<Item> items = CookingPots.getResolvedItems(entity.contents);
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

        matrices.translate(0, 0.52 + (((ticks > 180 ? (360 - ticks) : ticks) / 180.0F) * 0.39F), 0);
        matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(ticks));

        ticks += 0.5F;

        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.hashCode());
        matrices.pop();


        Optional<PlayerEntity> optPlayer = ClientHelper.getPlayer();
        if (!optPlayer.isPresent())
            return;

        PlayerEntity player = optPlayer.get();
        BlockEntityRenderDispatcher dispatcher = context.getRenderDispatcher();
        Camera camera = dispatcher.camera;

        double distance = ClientHelper.getBlockEntityDistance(player, entity, camera);
        List<Text> text = new ArrayList<>();

        if (entity.name != null && !entity.name.isEmpty()) {
            text.add(new LiteralText(entity.name));
        }
        if (entity.portions > 0) {
            text.add(new TranslatableText("gui.charm.cooking_pot_capacity", entity.portions));
        }

        if (distance < StorageLabels.viewDistance && !text.isEmpty()) {
            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, text);
        }
    }
}
