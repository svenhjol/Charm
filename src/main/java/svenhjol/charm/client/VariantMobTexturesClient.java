package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import svenhjol.charm.module.VariantMobTextures;
import svenhjol.charm.render.VariantMobRenderer;
import svenhjol.charm.base.CharmModule;

public class VariantMobTexturesClient {
    public VariantMobTexturesClient(CharmModule module) {
        if (!module.enabled)
            return;

        if (VariantMobTextures.variantChickens)
            EntityRendererRegistry.INSTANCE.register(EntityType.CHICKEN, ((dispatcher, context)
                -> new VariantMobRenderer.Chicken(dispatcher)));

        if (VariantMobTextures.variantCows)
            EntityRendererRegistry.INSTANCE.register(EntityType.COW, ((dispatcher, context)
                -> new VariantMobRenderer.Cow(dispatcher)));

        if (VariantMobTextures.variantPigs)
            EntityRendererRegistry.INSTANCE.register(EntityType.PIG, ((dispatcher, context)
                -> new VariantMobRenderer.Pig(dispatcher)));

        if (VariantMobTextures.variantSheep)
            EntityRendererRegistry.INSTANCE.register(EntityType.SHEEP, ((dispatcher, context)
                -> new VariantMobRenderer.Sheep(dispatcher)));

        if (VariantMobTextures.variantSnowGolems)
            EntityRendererRegistry.INSTANCE.register(EntityType.SNOW_GOLEM, ((dispatcher, context)
                -> new VariantMobRenderer.SnowGolem(dispatcher)));

        if (VariantMobTextures.variantSquids)
            EntityRendererRegistry.INSTANCE.register(EntityType.SQUID, ((dispatcher, context)
                -> new VariantMobRenderer.Squid(dispatcher)));

        if (VariantMobTextures.variantWolves)
            EntityRendererRegistry.INSTANCE.register(EntityType.WOLF, ((dispatcher, context)
                -> new VariantMobRenderer.Wolf(dispatcher)));

    }
}
