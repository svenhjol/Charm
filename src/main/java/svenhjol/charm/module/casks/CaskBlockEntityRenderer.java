package svenhjol.charm.module.casks;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.storage_labels.StorageLabels;
import svenhjol.charm.module.storage_labels.StorageLabelsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

        if (!Casks.showLabel)
            return;

        List<Text> text = new ArrayList<>();

        if (entity.name != null && !entity.name.isEmpty()) {
            text.add(new LiteralText(entity.name));
        }
        if (entity.portions > 0) {
            text.add(new TranslatableText("gui.charm.cask_capacity", entity.portions));
        }

        Optional<PlayerEntity> optPlayer = ClientHelper.getPlayer();
        if (!optPlayer.isPresent())
            return;

        PlayerEntity player = optPlayer.get();

        BlockEntityRenderDispatcher dispatcher = context.getRenderDispatcher();
        Camera camera = dispatcher.camera;

        double distance = ClientHelper.getBlockEntityDistance(player, entity, camera);

        if (distance < StorageLabels.viewDistance && !text.isEmpty()) {
            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, text);
        }
    }
}
