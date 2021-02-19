package svenhjol.charm.render;

import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.util.Identifier;
import svenhjol.charm.client.VariantMobTexturesClient;
import svenhjol.charm.mixin.accessor.EntityRenderDispatcherAccessor;
import svenhjol.charm.mixin.accessor.FeatureRendererAccessor;
import svenhjol.charm.mixin.accessor.LivingEntityRendererAccessor;

import java.util.List;

public class VariantMobRenderer {
    private static <T extends LivingEntity, M extends EntityModel<T>> void fillLayersFromOld(EntityRenderDispatcher manager, LivingEntityRenderer<T, M> renderer, EntityType<T> type) {
        EntityRenderer<?> old = ((EntityRenderDispatcherAccessor)manager).getRenderers().get(type);
        if (old != null) {
            List<FeatureRenderer<T, M>> layerRenderers = ((LivingEntityRendererAccessor<T, M>) renderer).getFeatures();
            layerRenderers.clear();
            ((LivingEntityRendererAccessor<T, M>) old).getFeatures()
                .stream()
                .peek(it -> ((FeatureRendererAccessor<T, M>)it).setContext(renderer))
                .forEach(layerRenderers::add);
        }
    }

    public static class Chicken extends ChickenEntityRenderer {
        public Chicken(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.CHICKEN);
        }

        @Override
        public Identifier getTexture(ChickenEntity entity) {
            return VariantMobTexturesClient.getChickenTexture(entity);
        }
    }

    public static class Cow extends CowEntityRenderer {
        public Cow(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.COW);
        }

        @Override
        public Identifier getTexture(CowEntity entity) {
            return VariantMobTexturesClient.getCowTexture(entity);
        }
    }

    public static class Pig extends PigEntityRenderer {
        public Pig(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.PIG);
        }

        @Override
        public Identifier getTexture(PigEntity entity) {
            return VariantMobTexturesClient.getPigTexture(entity);
        }
    }

    public static class Sheep extends SheepEntityRenderer {
        public Sheep(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.SHEEP);
        }

        @Override
        public Identifier getTexture(SheepEntity entity) {
            return VariantMobTexturesClient.getSheepTexture(entity);
        }
    }

    public static class SnowGolem extends SnowGolemEntityRenderer {
        public SnowGolem(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.SNOW_GOLEM);
        }

        @Override
        public Identifier getTexture(SnowGolemEntity entity) {
            return VariantMobTexturesClient.getSnowGolemTexture(entity);
        }
    }

    public static class Squid extends SquidEntityRenderer {
        public Squid(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.SQUID);
        }

        @Override
        public Identifier getTexture(SquidEntity entity) {
            return VariantMobTexturesClient.getSquidTexture(entity);
        }
    }

    public static class Wolf extends WolfEntityRenderer {
        public Wolf(EntityRenderDispatcher dispatcher) {
            super(dispatcher);
            fillLayersFromOld(dispatcher, this, EntityType.WOLF);
        }

        @Override
        public Identifier getTexture(WolfEntity entity) {
            return VariantMobTexturesClient.getWolfTexture(entity);
        }
    }
}
