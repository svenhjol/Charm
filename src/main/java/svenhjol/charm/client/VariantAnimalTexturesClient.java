package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import svenhjol.charm.module.VariantAnimalTextures;
import svenhjol.charm.render.VariantAnimalsRenderer;
import svenhjol.meson.MesonModule;

public class VariantAnimalTexturesClient {
    public VariantAnimalTexturesClient(MesonModule module) {
        if (VariantAnimalTextures.variantChickens)
            EntityRendererRegistry.INSTANCE.register(EntityType.CHICKEN, ((dispatcher, context)
                -> new VariantAnimalsRenderer.Chicken(dispatcher)));

        if (VariantAnimalTextures.variantCows)
            EntityRendererRegistry.INSTANCE.register(EntityType.COW, ((dispatcher, context)
                -> new VariantAnimalsRenderer.Cow(dispatcher)));

        if (VariantAnimalTextures.variantPigs)
            EntityRendererRegistry.INSTANCE.register(EntityType.PIG, ((dispatcher, context)
                -> new VariantAnimalsRenderer.Pig(dispatcher)));

        if (VariantAnimalTextures.variantSquids)
            EntityRendererRegistry.INSTANCE.register(EntityType.SQUID, ((dispatcher, context)
                -> new VariantAnimalsRenderer.Squid(dispatcher)));

        if (VariantAnimalTextures.variantWolves)
            EntityRendererRegistry.INSTANCE.register(EntityType.WOLF, ((dispatcher, context)
                -> new VariantAnimalsRenderer.Wolf(dispatcher)));
    }
}
