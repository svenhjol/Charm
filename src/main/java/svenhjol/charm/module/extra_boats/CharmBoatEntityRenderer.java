package svenhjol.charm.module.extra_boats;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import svenhjol.charm.Charm;

import java.util.Map;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class CharmBoatEntityRenderer extends EntityRenderer<CharmBoatEntity> {
   private final Map<CharmBoatEntity.BoatType, Pair<Identifier, BoatEntityModel>> texturesAndModels;

   public CharmBoatEntityRenderer(EntityRendererFactory.Context context) {
      super(context);
      this.shadowRadius = 0.8F;
      this.texturesAndModels = Stream.of(CharmBoatEntity.BoatType.values()).collect(ImmutableMap.toImmutableMap((type) -> {
         return type;
      }, (type) -> {
         return Pair.of(new Identifier(Charm.MOD_ID, "textures/entity/boat/" + type.asString() + ".png"), new BoatEntityModel(context.getPart(new EntityModelLayer(new Identifier(Charm.MOD_ID, "boat/" + type.asString()), "main"))));
      }));
   }

   public void render(CharmBoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
      matrixStack.push();
      matrixStack.translate(0.0D, 0.375D, 0.0D);
      matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - f));
      float h = (float)boatEntity.getDamageWobbleTicks() - g;
      float j = boatEntity.getDamageWobbleStrength() - g;
      if (j < 0.0F) {
         j = 0.0F;
      }

      if (h > 0.0F) {
         matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(MathHelper.sin(h) * h * j / 10.0F * (float)boatEntity.getDamageWobbleSide()));
      }

      float k = boatEntity.interpolateBubbleWobble(g);
      if (!MathHelper.approximatelyEquals(k, 0.0F)) {
         matrixStack.multiply(new Quaternion(new Vec3f(1.0F, 0.0F, 1.0F), boatEntity.interpolateBubbleWobble(g), true));
      }

      Pair<Identifier, BoatEntityModel> pair = this.texturesAndModels.get(boatEntity.getCharmBoatType());
      Identifier identifier = pair.getFirst();
      BoatEntityModel boatEntityModel = pair.getSecond();
      matrixStack.scale(-1.0F, -1.0F, 1.0F);
      matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(90.0F));
      boatEntityModel.setAngles(boatEntity, g, 0.0F, -0.1F, 0.0F, 0.0F);
      VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(boatEntityModel.getLayer(identifier));
      boatEntityModel.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
      if (!boatEntity.isSubmergedInWater()) {
         VertexConsumer vertexConsumer2 = vertexConsumerProvider.getBuffer(RenderLayer.getWaterMask());
         boatEntityModel.getBottom().render(matrixStack, vertexConsumer2, i, OverlayTexture.DEFAULT_UV);
      }

      matrixStack.pop();
      super.render(boatEntity, f, g, matrixStack, vertexConsumerProvider, i);
   }

   public Identifier getTexture(CharmBoatEntity boatEntity) {
      return this.texturesAndModels.get(boatEntity.getCharmBoatType()).getFirst();
   }
}
