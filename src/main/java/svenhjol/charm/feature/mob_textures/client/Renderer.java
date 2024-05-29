package svenhjol.charm.feature.mob_textures.client;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.npc.WanderingTrader;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.mob_textures.MobTextures;

public class Renderer {
    private static final MobTextures MOB_TEXTURES = Resolve.feature(MobTextures.class);

    @SuppressWarnings("unchecked")
    private static <T extends LivingEntity, M extends EntityModel<T>> void fillLayersFromOld(EntityRendererProvider.Context context, LivingEntityRenderer<T, M> renderer, EntityType<T> type) {
        EntityRenderer<?> old = context.getEntityRenderDispatcher().renderers.get(type);

        if (old != null) {
            var layerRenderers = renderer.layers;
            layerRenderers.clear();

            ((LivingEntityRenderer<T, M>)old).layers
                .stream()
                .peek(layer -> layer.renderer = renderer)
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
            return MOB_TEXTURES.handlers.chickenTexture(entity);
        }
    }

    public static class RenderCow extends CowRenderer {
        public RenderCow(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.COW);
        }

        @Override
        public ResourceLocation getTextureLocation(Cow entity) {
            return MOB_TEXTURES.handlers.cowTexture(entity);
        }
    }

    public static class RenderDolphin extends DolphinRenderer {
        public RenderDolphin(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.DOLPHIN);
        }

        @Override
        public ResourceLocation getTextureLocation(Dolphin entity) {
            return MOB_TEXTURES.handlers.dolphinTexture(entity);
        }
    }

    public static class RenderPig extends PigRenderer {
        public RenderPig(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.PIG);
        }

        @Override
        public ResourceLocation getTextureLocation(Pig entity) {
            return MOB_TEXTURES.handlers.pigTexture(entity);
        }
    }

    public static class RenderSheep extends SheepRenderer {
        public RenderSheep(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.SHEEP);
        }

        @Override
        public ResourceLocation getTextureLocation(Sheep entity) {
            return MOB_TEXTURES.handlers.sheepTexture(entity);
        }
    }

    public static class RenderSnowGolem extends SnowGolemRenderer {
        public RenderSnowGolem(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.SNOW_GOLEM);
        }

        @Override
        public ResourceLocation getTextureLocation(SnowGolem entity) {
            return MOB_TEXTURES.handlers.snowGolemTexture(entity);
        }
    }

    public static class RenderSquid extends SquidRenderer<Squid> {
        public RenderSquid(EntityRendererProvider.Context context) {
            super(context, new SquidModel<>(context.bakeLayer(ModelLayers.SQUID)));
            fillLayersFromOld(context, this, EntityType.SQUID);
        }

        @Override
        public ResourceLocation getTextureLocation(Squid entity) {
            return MOB_TEXTURES.handlers.squidTexture(entity);
        }
    }

    public static class RenderTurtle extends TurtleRenderer {
        public RenderTurtle(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.TURTLE);
        }

        @Override
        public ResourceLocation getTextureLocation(Turtle entity) {
            return MOB_TEXTURES.handlers.turtleTexture(entity);
        }
    }

    public static class RenderWanderingTrader extends WanderingTraderRenderer {
        public RenderWanderingTrader(EntityRendererProvider.Context context) {
            super(context);
            fillLayersFromOld(context, this, EntityType.WANDERING_TRADER);
        }

        @Override
        public ResourceLocation getTextureLocation(WanderingTrader entity) {
            return MOB_TEXTURES.handlers.wanderingTraderTexture(entity);
        }
    }
}
