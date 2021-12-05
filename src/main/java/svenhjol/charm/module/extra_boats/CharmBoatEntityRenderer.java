package svenhjol.charm.module.extra_boats;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import svenhjol.charm.Charm;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class CharmBoatEntityRenderer extends EntityRenderer<CharmBoatEntity> {
    private final Map<String, Pair<ResourceLocation, BoatModel>> texturesAndModels;

    public CharmBoatEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.8F;
        this.texturesAndModels = ExtraBoats.BOATS.keySet().stream().collect(ImmutableMap.toImmutableMap(
            (type) -> type,
            (type) -> Pair.of(
                new ResourceLocation(Charm.MOD_ID, "textures/entity/boat/" + type + ".png"),
                new BoatModel(context.bakeLayer(new ModelLayerLocation(new ResourceLocation(Charm.MOD_ID, "boat/" + type), "main"))))));
    }

    public void render(CharmBoatEntity boatEntity, float f, float g, PoseStack poseStack, MultiBufferSource bufferSource, int i) {
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.375D, 0.0D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - f));
        float h = (float) boatEntity.getHurtTime() - g;
        float j = boatEntity.getDamage() - g;
        if (j < 0.0F) {
            j = 0.0F;
        }

        if (h > 0.0F) {
            poseStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(h) * h * j / 10.0F * (float) boatEntity.getHurtDir()));
        }

        float k = boatEntity.getBubbleAngle(g);
        if (!Mth.equal(k, 0.0F)) {
            poseStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), boatEntity.getBubbleAngle(g), true));
        }

        Pair<ResourceLocation, BoatModel> pair = this.texturesAndModels.get(boatEntity.getCharmBoatType());
        ResourceLocation identifier = pair.getFirst();
        BoatModel boatEntityModel = pair.getSecond();
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
        boatEntityModel.setupAnim(boatEntity, g, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(boatEntityModel.renderType(identifier));
        boatEntityModel.renderToBuffer(poseStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        if (!boatEntity.isUnderWater()) {
            VertexConsumer vertexConsumer2 = bufferSource.getBuffer(RenderType.waterMask());
            boatEntityModel.waterPatch().render(poseStack, vertexConsumer2, i, OverlayTexture.NO_OVERLAY);
        }

        poseStack.popPose();
        super.render(boatEntity, f, g, poseStack, bufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(CharmBoatEntity boatEntity) {
        return this.texturesAndModels.get(boatEntity.getCharmBoatType()).getFirst();
    }
}
