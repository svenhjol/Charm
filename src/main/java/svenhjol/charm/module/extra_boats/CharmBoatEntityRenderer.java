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
import svenhjol.charm.module.extra_boats.CharmBoatEntity;

import java.util.Map;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class CharmBoatEntityRenderer extends EntityRenderer<svenhjol.charm.module.extra_boats.CharmBoatEntity> {
   private final Map<svenhjol.charm.module.extra_boats.CharmBoatEntity.BoatType, Pair<ResourceLocation, BoatModel>> texturesAndModels;

   public CharmBoatEntityRenderer(EntityRendererProvider.Context context) {
      super(context);
      this.shadowRadius = 0.8F;
      this.texturesAndModels = Stream.of(svenhjol.charm.module.extra_boats.CharmBoatEntity.BoatType.values()).collect(ImmutableMap.toImmutableMap((type) -> {
         return type;
      }, (type) -> {
         return Pair.of(new ResourceLocation(Charm.MOD_ID, "textures/entity/boat/" + type.asString() + ".png"), new BoatModel(context.bakeLayer(new ModelLayerLocation(new ResourceLocation(Charm.MOD_ID, "boat/" + type.asString()), "main"))));
      }));
   }

   public void render(svenhjol.charm.module.extra_boats.CharmBoatEntity boatEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
      matrixStack.pushPose();
      matrixStack.translate(0.0D, 0.375D, 0.0D);
      matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - f));
      float h = (float)boatEntity.getHurtTime() - g;
      float j = boatEntity.getDamage() - g;
      if (j < 0.0F) {
         j = 0.0F;
      }

      if (h > 0.0F) {
         matrixStack.mulPose(Vector3f.XP.rotationDegrees(Mth.sin(h) * h * j / 10.0F * (float)boatEntity.getHurtDir()));
      }

      float k = boatEntity.getBubbleAngle(g);
      if (!Mth.equal(k, 0.0F)) {
         matrixStack.mulPose(new Quaternion(new Vector3f(1.0F, 0.0F, 1.0F), boatEntity.getBubbleAngle(g), true));
      }

      Pair<ResourceLocation, BoatModel> pair = this.texturesAndModels.get(boatEntity.getCharmBoatType());
      ResourceLocation identifier = pair.getFirst();
      BoatModel boatEntityModel = pair.getSecond();
      matrixStack.scale(-1.0F, -1.0F, 1.0F);
      matrixStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));
      boatEntityModel.setupAnim(boatEntity, g, 0.0F, -0.1F, 0.0F, 0.0F);
      VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(boatEntityModel.renderType(identifier));
      boatEntityModel.renderToBuffer(matrixStack, vertexConsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      if (!boatEntity.isUnderWater()) {
         VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderType.waterMask());
         boatEntityModel.waterPatch().render(matrixStack, vertexConsumer2, i, OverlayTexture.NO_OVERLAY);
      }

      matrixStack.popPose();
      super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);
   }

   public ResourceLocation getTexture(CharmBoatEntity boatEntity) {
      return this.texturesAndModels.get(boatEntity.getCharmBoatType()).getFirst();
   }
}
