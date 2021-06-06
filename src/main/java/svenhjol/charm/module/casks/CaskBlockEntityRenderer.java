package svenhjol.charm.module.casks;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.storage_labels.StorageLabels;
import svenhjol.charm.module.storage_labels.StorageLabelsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class CaskBlockEntityRenderer<T extends CaskBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererProvider.Context context;

    public CaskBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return true;
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (entity == null)
            return;

        if (!Casks.showLabel)
            return;

        List<Component> text = new ArrayList<>();

        if (entity.name != null && !entity.name.isEmpty()) {
            text.add(new TextComponent(entity.name));
        }
        if (entity.portions > 0) {
            text.add(new TranslatableComponent("gui.charm.cask_capacity", entity.portions));
        }

        Optional<Player> optPlayer = ClientHelper.getPlayer();
        if (!optPlayer.isPresent())
            return;

        Player player = optPlayer.get();

        BlockEntityRenderDispatcher dispatcher = context.getBlockEntityRenderDispatcher();
        Camera camera = dispatcher.camera;

        double distance = ClientHelper.getBlockEntityDistance(player, entity, camera);

        if (distance < StorageLabels.viewDistance && !text.isEmpty()) {
            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, text);
        }
    }
}
