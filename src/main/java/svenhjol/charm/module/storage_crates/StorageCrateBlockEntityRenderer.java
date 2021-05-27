package svenhjol.charm.module.storage_crates;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import svenhjol.charm.module.storage_labels.StorageLabelsClient;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.util.math.Direction.DOWN;

@Environment(EnvType.CLIENT)
public class StorageCrateBlockEntityRenderer<T extends StorageCrateBlockEntity> implements BlockEntityRenderer<T> {
    private static final int PER_ROW = 6;

    protected World world;
    protected ItemStack stack;
    protected ItemRenderer itemRenderer;
    protected TextRenderer textRenderer;
    protected BlockEntityRendererFactory.Context context;

    public StorageCrateBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        this.context = context;
    }

    @Override
    public boolean rendersOutsideBoundingBox(T blockEntity) {
        return false;
    }

    @Override
    public void render(T crate, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();

        world = crate.getWorld();
        if (world == null)
            return;

        PlayerEntity player = client.player;

        Item item = crate.getItemType();
        if (item == null)
            return;

        int count = Math.min(Math.max(1, crate.filledStacks()), PER_ROW * 3);

        stack = new ItemStack(item);
        itemRenderer = client.getItemRenderer();
        textRenderer = client.textRenderer;

        int distCutoffRender = 84;
        int distFullRender = 32;

        BlockPos pos = crate.getPos();
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        BlockState state = world.getBlockState(pos);
        if (!(state.getBlock() instanceof StorageCrateBlock))
            return;

        boolean isSolidBlock = stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock().getDefaultState().isSolidBlock(world, BlockPos.ORIGIN);
        Direction facing = state.get(StorageCrateBlock.FACING);

        BlockEntityRenderDispatcher dispatcher = this.context.getRenderDispatcher();
        Camera camera = dispatcher.camera;

        double d = camera.getPos().squaredDistanceTo(x, y, z);

        if (d > distCutoffRender)
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

            if (d < distFullRender || l == 0) {
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
                                rotation = Vec3f.POSITIVE_X.getDegreesQuaternion(90);

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
                                        rotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(90);
                                    break;
                                case WEST:
                                    yo = 0.01D + coords[i] * scaleMultiplier;
                                    xo = 2.2D - (i * overlap) - depth;
                                    if (!isSolidBlock)
                                        rotation = Vec3f.POSITIVE_Y.getDegreesQuaternion(90);
                                    break;
                            }
                        }

                        renderItemStack(matrices, vertexConsumers, rotation, xo, yo, zo, scale, layerLight);
                    }
                }
            }

            remainder = count - (l * PER_ROW);
        }

        if (player != null && !crate.isEmpty()) {
            LiteralText text = new LiteralText(String.valueOf(crate.getTotalNumberOfItems()));
            StorageLabelsClient.renderLabel(matrices, vertexConsumers, player, camera, text);
        }
    }

    private void renderItemStack(MatrixStack matrices, VertexConsumerProvider vertexConsumers, @Nullable Quaternion rotation, double x, double y, double z, float scale, int light) {
        matrices.push();
        matrices.scale(scale, scale, scale);

        matrices.translate(x, y, z);

        if (rotation != null)
            matrices.multiply(rotation);

        itemRenderer.renderItem(stack, ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, stack.hashCode());
        matrices.pop();
    }
}


//        BakedModel model = itemRenderer.getHeldItemModel(stack, world, null, 1414);
//
//        if (mippedBlocks == null) {
//            mippedBlocks = new AbstractTexture() {
//
//                @Override
//                public void load(ResourceManager manager) throws IOException {
//                    clearGlId();
//                    SpriteAtlasTexture atlas = MinecraftClient.getInstance().getBakedModelManager().getAtlas(new Identifier("textures/atlas/blocks.png"));
//                    GlStateManager._bindTexture(atlas.getGlId());
//                    int maxLevel = GL11.glGetTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL);
//                    if (maxLevel == 0 || !GL.getCapabilities().GL_ARB_copy_image) {
//                        int w = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
//                        int h = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
//                        ByteBuffer dest = MemoryUtil.memAlloc(w*h*4);
//                        try {
//                            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, NativeImage.Format.ABGR.getPixelDataFormat(), GL11.GL_UNSIGNED_BYTE, MemoryUtil.memAddress(dest));
//                        } catch (Error | RuntimeException e) {
//                            MemoryUtil.memFree(dest);
//                            throw e;
//                        }
//                        NativeImage img = NativeImageAccessor.invokeConstructor(NativeImage.Format.ABGR, w, h, false, MemoryUtil.memAddress(dest));
//                        try {
//                            NativeImage mipped = MipmapHelper.getMipmapLevelsImages(img, 0)[0];
//                            try {
//                                TextureUtil.prepareImage(getGlId(), mipped.getWidth(), mipped.getHeight());
//                                GlStateManager._bindTexture(getGlId());
//                                mipped.upload(0, 0, 0, true);
//                            } finally {
//                                mipped.close();
//                            }
//                        } finally {
//                            img.close();
//                        }
//                    } else {
//                        int w = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 1, GL11.GL_TEXTURE_WIDTH);
//                        int h = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 1, GL11.GL_TEXTURE_HEIGHT);
//                        TextureUtil.prepareImage(getGlId(), w, h);
//                        ARBCopyImage.glCopyImageSubData(
//                            atlas.getGlId(), GL11.GL_TEXTURE_2D, 1, 0, 0, 0,
//                            getGlId(), GL11.GL_TEXTURE_2D, 0, 0, 0, 0,
//                            w, h, 1);
//                    }
//                }
//            };
//            MinecraftClient.getInstance().getTextureManager().registerTexture(new Identifier("fabrication", "textures/atlas/blocks-mip.png"), mippedBlocks);
//        }
//
//
//        matrices.push();
//        matrices.scale(0.55F, 0.55F, 0.55F);
//        RenderLayer defLayer = RenderLayers.getItemLayer(stack, true);
//        RenderLayer layer = defLayer == TexturedRenderLayers.getEntityCutout() ?
//            RenderLayer.getEntityCutout(new Identifier("fabrication", "textures/atlas/blocks-mip.png")) :
//            RenderLayer.getEntityTranslucent(new Identifier("fabrication", "textures/atlas/blocks-mip.png"));
//        VertexConsumer vertices = vertexConsumers.getBuffer(layer);
//
//        ((ItemRendererAccessor)itemRenderer).invokeRenderBakedItemModel(model, stack, light, overlay, matrices, vertices);
//        matrices.pop();

//
//        boolean isBlockItem = item instanceof BlockItem && ((BlockItem)item).getBlock().getDefaultState().isSolidBlock(world, BlockPos.ORIGIN);
//        float scaleSize = isBlockItem ? 0.55F : 0.44F;
//        double scaleLayerHeight = isBlockItem ? 0.17D : 0.15D;
//
//
//        if (d < distCutoffRender) {
//            ItemStack stack = new ItemStack(item);
//
//            int maxLayers = isBlockItem ? 4 : 2;
//            int itemsPerLayer = isBlockItem ? 4 : 8;
//
//            int t = maxLayers * itemsPerLayer;
//            boolean firstpass = count <= t;
//
//            int maxCount = firstpass ? count : (t + 1 - itemsPerLayer) + ((count - 1) % itemsPerLayer);
//
//            int layer = 0;
//            int seed = 0;
//
//            ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
//            Random random = new Random();
//
//            for (int i = 0; i < t; i++) {
//                if (i % itemsPerLayer == 0) {
//                    layer = layer + 1;
//                }
//
//                seed = firstpass ? layer : layer + (int) Math.ceil(((double) (count - t) / itemsPerLayer));
//                random.setSeed((long) entity.hashCode() * (seed + 1));
//
//                int layerLight = (light >> (firstpass ? ((count - 1) / itemsPerLayer) / layer : maxLayers - layer)) - 1;
//
//                matrices.push();
//                matrices.translate(0.0F, 0.1D + (layer * scaleLayerHeight), 0.0F);
//
//                int f = maxCount - i;
////                int k = (firstpass || layer >= maxLayers - 2) ? 1 : 2;
//
//
//                int k = 1;
//                if (d > distFullRender && !firstpass && layer < maxLayers)
//                    k = 4;
//
//                float s = scaleSize - (0.02F * (maxLayers - layer));
//
//                for (int j = 0; j < Math.min(itemsPerLayer, f); j = j + k) {
//                    matrices.push();
//                    matrices.translate(0.2D + random.nextDouble() * 0.6D, 0.0D, 0.2D + random.nextDouble() * 0.6D);
//                    matrices.scale(s, s, s);
//                    matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(90));
//                    matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(-30 + (random.nextFloat() * 60)));
//                    matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(-180 + (random.nextFloat() * 360)));
//                    itemRenderer.renderItem(stack, ModelTransformation.Mode.FIXED, layerLight, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.hashCode());
//                    matrices.pop();
//                }
//
//                MatrixStack.Entry peek = matrices.peek();
//
//                matrices.pop();
//            }
//        }
