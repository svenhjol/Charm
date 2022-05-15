package svenhjol.charm.module.variant_mob_textures;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.npc.WanderingTrader;

import java.util.List;

public class VariantMobRenderer {
    private static <T extends LivingEntity, M extends EntityModel<T>> void fillLayersFromOld(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> renderer, EntityType<T> type) {
        EntityRenderer<?> old = context.getEntityRenderDispatcher().renderers.get(type);
        if (old != null) {
            List<RenderLayer<T, M>> layerRenderers = renderer.layers;
            layerRenderers.clear();
            ((LivingEntityRenderer<T, M>)old).layers
                .stream()
                .peek(it -> it.renderer = renderer)
                .forEach(layerRenderers::add);
        }
    }

    public static class RenderChicken extends ChickenRenderer {
        public RenderChicken(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.CHICKEN);
        }

        @Override
        public ResourceLocation getTextureLocation(Chicken entity) {
            return VariantMobTexturesClient.getChickenTexture(entity);
        }
    }

    public static class RenderCow extends CowRenderer {
        public RenderCow(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.COW);
        }

        @Override
        public ResourceLocation getTextureLocation(Cow entity) {
            return VariantMobTexturesClient.getCowTexture(entity);
        }
    }

    public static class RenderDolphin extends DolphinRenderer {
        public RenderDolphin(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.DOLPHIN);
        }

        @Override
        public ResourceLocation getTextureLocation(Dolphin entity) {
            return VariantMobTexturesClient.getDolphinTexture(entity);
        }
    }

    public static class RenderPig extends PigRenderer {
        public RenderPig(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.PIG);
        }

        @Override
        public ResourceLocation getTextureLocation(Pig entity) {
            return VariantMobTexturesClient.getPigTexture(entity);
        }
    }

    public static class RenderSheep extends SheepRenderer {
        public RenderSheep(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.SHEEP);
        }

        @Override
        public ResourceLocation getTextureLocation(Sheep entity) {
            return VariantMobTexturesClient.getSheepTexture(entity);
        }
    }

    public static class RenderSnowGolem extends SnowGolemRenderer {
        public RenderSnowGolem(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.SNOW_GOLEM);
        }

        @Override
        public ResourceLocation getTextureLocation(SnowGolem entity) {
            return VariantMobTexturesClient.getSnowGolemTexture(entity);
        }
    }

    public static class RenderSquid extends SquidRenderer {
        public RenderSquid(EntityRendererProvider.Context context) {
            super(context, new SquidModel(context.bakeLayer(ModelLayers.SQUID)));
            fillLayersFromOld(context, this, EntityType.SQUID);
        }

        @Override
        public ResourceLocation getTextureLocation(Squid entity) {
            return VariantMobTexturesClient.getSquidTexture(entity);
        }
    }

    public static class RenderTurtle extends TurtleRenderer {
        public RenderTurtle(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.TURTLE);
        }

        @Override
        public ResourceLocation getTextureLocation(Turtle entity) {
            return VariantMobTexturesClient.getTurtleTexture(entity);
        }
    }

    public static class RenderWolf extends WolfRenderer {
        public RenderWolf(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.WOLF);
        }

        @Override
        public ResourceLocation getTextureLocation(Wolf entity) {
            return VariantMobTexturesClient.getWolfTexture(entity);
        }
    }

    public static class RenderWanderingTrader extends WanderingTraderRenderer {
        public RenderWanderingTrader(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.WANDERING_TRADER);
        }

        @Override
        public ResourceLocation getTextureLocation(WanderingTrader entity) {
            return VariantMobTexturesClient.getWanderingTraderTexture(entity);
        }
    }
}
