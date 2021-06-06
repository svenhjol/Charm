package svenhjol.charm.module.variant_mob_textures;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.CowRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.PigRenderer;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.client.renderer.entity.SnowGolemRenderer;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.entity.passive.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import svenhjol.charm.module.variant_mob_textures.VariantMobTexturesClient;
import svenhjol.charm.mixin.accessor.EntityRenderDispatcherAccessor;
import svenhjol.charm.mixin.accessor.FeatureRendererAccessor;
import svenhjol.charm.mixin.accessor.LivingEntityRendererAccessor;

import java.util.List;

public class VariantMobRenderer {
    private static <T extends LivingEntity, M extends EntityModel<T>> void fillLayersFromOld(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> renderer, EntityType<T> type) {
        EntityRenderer<?> old = ((EntityRenderDispatcherAccessor)context.getEntityRenderDispatcher()).getRenderers().get(type);
        if (old != null) {
            List<RenderLayer<T, M>> layerRenderers = ((LivingEntityRendererAccessor<T, M>) renderer).getFeatures();
            layerRenderers.clear();
            ((LivingEntityRendererAccessor<T, M>) old).getFeatures()
                .stream()
                .peek(it -> ((FeatureRendererAccessor<T, M>)it).setContext(renderer))
                .forEach(layerRenderers::add);
        }
    }

    public static class Chicken extends ChickenRenderer {
        public Chicken(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.CHICKEN);
        }

        @Override
        public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Chicken entity) {
            return VariantMobTexturesClient.getChickenTexture(entity);
        }
    }

    public static class Cow extends CowRenderer {
        public Cow(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.COW);
        }

        @Override
        public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Cow entity) {
            return VariantMobTexturesClient.getCowTexture(entity);
        }
    }

    public static class Pig extends PigRenderer {
        public Pig(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.PIG);
        }

        @Override
        public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Pig entity) {
            return VariantMobTexturesClient.getPigTexture(entity);
        }
    }

    public static class Sheep extends SheepRenderer {
        public Sheep(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.SHEEP);
        }

        @Override
        public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Sheep entity) {
            return VariantMobTexturesClient.getSheepTexture(entity);
        }
    }

    public static class SnowGolem extends SnowGolemRenderer {
        public SnowGolem(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.SNOW_GOLEM);
        }

        @Override
        public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.SnowGolem entity) {
            return VariantMobTexturesClient.getSnowGolemTexture(entity);
        }
    }

    public static class Squid extends SquidRenderer {
        public Squid(EntityRendererProvider.Context context) {
            super(context, new SquidModel(context.bakeLayer(ModelLayers.SQUID)));
            fillLayersFromOld(context, this, EntityType.SQUID);
        }

        @Override
        public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Squid entity) {
            return VariantMobTexturesClient.getSquidTexture(entity);
        }
    }

    public static class Wolf extends WolfRenderer {
        public Wolf(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.WOLF);
        }

        @Override
        public ResourceLocation getTextureLocation(net.minecraft.world.entity.animal.Wolf entity) {
            return VariantMobTexturesClient.getWolfTexture(entity);
        }
    }
}
