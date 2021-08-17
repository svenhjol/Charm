package svenhjol.charm.module.storage_crates;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.module.storage_labels.StorageLabels;
import svenhjol.charm.module.storage_labels.StorageLabelsClient;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static net.minecraft.core.Direction.DOWN;

@SuppressWarnings("NullableProblems")
@Environment(EnvType.CLIENT)
public class StorageCrateBlockEntityRenderer<T extends StorageCrateBlockEntity> implements BlockEntityRenderer<T> {
    private static final int PER_ROW = 6;
    private static final int DIST_CUTOFF_DEFAULT = 400;
    private static final int DIST_CUTOFF_FABULOUS = 900;
    private static final int DIST_FULLRENDER_DEFAULT = 100;
    private static final int DIST_FULLRENDER_FABULOUS = 160;

    protected Level world;
    protected ItemStack stack;
    protected ItemRenderer itemRenderer;
    protected Font textRenderer;
    protected BlockEntityRendererProvider.Context context;

    public StorageCrateBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }

    @Override
    public boolean shouldRenderOffScreen(T blockEntity) {
        return true;
    }

    @Override
    public void render(T crate, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        Minecraft client = Minecraft.getInstance();

        if (!StorageCrates.showLabel)
            return;

        world = crate.getLevel();
        if (world == null)
            return;

        Player player = client.player;
        int count = Math.min(Math.max(1, crate.filledStacks()), PER_ROW * 3);

        stack = crate.getItemType();
        if (stack == null || stack.isEmpty())
            return;

        boolean fabulous = Minecraft.useShaderTransparency();
        int distCutoffRender = fabulous ? DIST_CUTOFF_FABULOUS : DIST_CUTOFF_DEFAULT;
        int distFullRender = fabulous ? DIST_FULLRENDER_FABULOUS : DIST_FULLRENDER_DEFAULT;

        itemRenderer = client.getItemRenderer();
        textRenderer = client.font;

        BlockPos pos = crate.getBlockPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof StorageCrateBlock))
            return;

        boolean isSolidBlock = stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock().defaultBlockState().isRedstoneConductor(world, BlockPos.ZERO);
        Direction facing = state.getValue(StorageCrateBlock.FACING);

        BlockEntityRenderDispatcher dispatcher = this.context.getBlockEntityRenderDispatcher();
        Camera camera = dispatcher.camera;

        double distance = camera.getPosition().distanceToSqr(x, y, z);
        if (distance > distCutoffRender)
            return;

        Random random = new Random();
        random.setSeed((long) crate.hashCode() + count);


        double[] coords;
        int layers;
        int remainder;

        if (count <= PER_ROW) {
            layers = 1;
            remainder = count;
        } else if (count <= (PER_ROW * 2)) {
            layers = 2;
            remainder = count - PER_ROW;
        } else {
            layers = 3;
            remainder = count - (PER_ROW * 2);
        }

        for (int l = 0; l < layers; l++) {
            int layerLight = light >> l;

            // TODO dynamic generate item coords
            if (remainder == 1) {
                coords = new double[]{0.25D, 0.25D};
            } else if (remainder == 2) {
                coords = new double[]{0.25D, 0.25D, 0.50D, 0.25D};
            } else if (remainder == 3) {
                coords = new double[]{0.25D, 0.25D, 0.50D, 0.25D, 0.75D, 0.25D};
            } else if (remainder == 4) {
                coords = new double[]{0.25D, 0.25D, 0.50D, 0.25D, 0.75D, 0.25D, 0.25D, 0.75D};
            } else if (remainder == 5) {
                coords = new double[]{0.25D, 0.25D, 0.50D, 0.25D, 0.75D, 0.25D, 0.25D, 0.75D, 0.50D, 0.75D};
            } else {
                coords = new double[]{0.25D, 0.25D, 0.50D, 0.25D, 0.75D, 0.25D, 0.25D, 0.75D, 0.50D, 0.75D, 0.75D, 0.75D};
            }

            double xo = 0;
            double yo = 0;
            double zo = 0;
            double overlap = 0.001D;

            Quaternion rotation = null;

            if (distance < distFullRender || l == 0) {
                for (int i = 0; i < coords.length; i++) {
                    float scale = 0.5F - (Math.min(PER_ROW, remainder) * 0.008F);
                    double scaleMultiplier = (1.7D + scale);
                    double depth = (layers - l) * 0.55D;

                    if (i % 2 == 0) {
                        if (facing.getAxis().isVertical()) {
                            xo = coords[i] * scaleMultiplier;
                        } else {
                            switch (facing) {
                                case SOUTH:
                                    xo = coords[i] * scaleMultiplier;
                                    break;
                                case NORTH:
                                    xo = 2.2D - coords[i] * scaleMultiplier;
                                    break;
                                case EAST:
                                    zo = 2.2D - coords[i] * scaleMultiplier;
                                    break;
                                case WEST:
                                    zo = coords[i] * scaleMultiplier;
                                    break;
                            }
                        }
                    } else {
                        if (facing.getAxis().isVertical()) {
                            zo = coords[i] * scaleMultiplier;
                            yo = (i * overlap) + depth;
                            if (!isSolidBlock)
                                rotation = Vector3f.XP.rotationDegrees(90);

                            if (facing == DOWN)
                                yo = 2.2D - yo;

                        } else {
                            switch (facing) {
                                case SOUTH:
                                    yo = 0.01D + coords[i] * scaleMultiplier;
                                    zo = (i * overlap) + depth;
                                    break;
                                case NORTH:
                                    yo = 0.01D + coords[i] * scaleMultiplier;
                                    zo = 2.2D - (i * overlap) - depth;
                                    break;
                                case EAST:
                                    yo = 0.01D + coords[i] * scaleMultiplier;
                                    xo = (i * overlap) + depth;
                                    if (!isSolidBlock)
                                        rotation = Vector3f.YP.rotationDegrees(90);
                                    break;
                                case WEST:
                                    yo = 0.01D + coords[i] * scaleMultiplier;
                                    xo = 2.2D - (i * overlap) - depth;
                                    if (!isSolidBlock)
                                        rotation = Vector3f.YP.rotationDegrees(90);
                                    break;
                            }
                        }

                        renderItemStack(matrices, vertexConsumers, rotation, xo, yo, zo, scale, layerLight);
                    }
                }
            }

            remainder = count - (l * PER_ROW);
        }

        if (distance < StorageLabels.viewDistance && player != null && !crate.isEmpty()) {
            List<Component> text = new ArrayList<>();
            text.add(new TranslatableComponent(crate.getItemType().getDescriptionId()));

            if (crate.getTotalNumberOfItems() > 0)
                text.add(new TranslatableComponent("gui.charm.storage_crate_capacity", String.valueOf(crate.getTotalNumberOfItems())));

            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, text);
        }
    }

    private void renderItemStack(PoseStack matrices, MultiBufferSource vertexConsumers, @Nullable Quaternion rotation, double x, double y, double z, float scale, int light) {
        matrices.pushPose();
        matrices.scale(scale, scale, scale);

        matrices.translate(x, y, z);

        if (rotation != null)
            matrices.mulPose(rotation);

        itemRenderer.renderStatic(stack, ItemTransforms.TransformType.FIXED, light, OverlayTexture.NO_OVERLAY, matrices, vertexConsumers, stack.hashCode());
        matrices.popPose();
    }
}
