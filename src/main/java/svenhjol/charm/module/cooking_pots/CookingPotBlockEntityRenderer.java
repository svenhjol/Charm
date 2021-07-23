package svenhjol.charm.module.cooking_pots;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.storage_labels.StorageLabels;
import svenhjol.charm.module.storage_labels.StorageLabelsClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SuppressWarnings("ClassCanBeRecord")
@Environment(EnvType.CLIENT)
public class CookingPotBlockEntityRenderer<T extends CookingPotBlockEntity> implements BlockEntityRenderer<T> {
    private final BlockEntityRendererProvider.Context context;

    public CookingPotBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return true;
    }

    @Override
    public void render(T entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Level world = entity.getLevel();
        if (world == null)
            return;

        if (!CookingPots.showLabel)
            return;

        List<Item> items = CookingPots.getResolvedItems(entity.contents);
        if (items.isEmpty())
            return;

        matrices.pushPose();
        matrices.scale(0.57F, 0.57F, 0.57F);
        matrices.translate(0.82F, 0.68F, 0.82F);

        if (entity.displayTicks > 360) {
            entity.displayTicks = 0;
            if (entity.contents.size() > 1) {
                entity.displayIndex++;
                if (entity.displayIndex >= entity.contents.size())
                    entity.displayIndex = 0;
            } else {
                entity.displayIndex = 0;
            }
        }

        ItemStack stack;
        try {
            stack = new ItemStack(items.get(entity.displayIndex));
        } catch (IndexOutOfBoundsException e) {
            entity.displayIndex = 0;
            matrices.popPose();
            return;
        }

        matrices.translate(0, 0.52 + (((entity.displayTicks > 180 ? (360 - entity.displayTicks) : entity.displayTicks) / 180.0F) * 0.39F), 0);
        matrices.mulPose(Vector3f.YP.rotationDegrees(entity.displayTicks));

        entity.displayTicks += 0.5F;

        Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, entity.hashCode());
        matrices.popPose();


        Optional<Player> optPlayer = ClientHelper.getPlayer();
        if (optPlayer.isEmpty())
            return;

        Player player = optPlayer.get();
        BlockEntityRenderDispatcher dispatcher = context.getBlockEntityRenderDispatcher();
        Camera camera = dispatcher.camera;

        double distance = ClientHelper.getBlockEntityDistance(player, entity, camera);
        List<Component> text = new ArrayList<>();

        if (entity.name != null && !entity.name.isEmpty()) {
            text.add(new TextComponent(entity.name));
        }
        if (entity.portions > 0) {
            text.add(new TranslatableComponent("gui.charm.cooking_pot_capacity", entity.portions));
        }

        if (distance < StorageLabels.viewDistance && !text.isEmpty()) {
            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, text);
        }
    }
}
