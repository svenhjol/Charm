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
            EntityRendererRegistry.INSTANCE.register(EntityType.CHICKEN, VariantMobRenderer.Chicken::new);

        if (VariantMobTextures.variantCows)
            EntityRendererRegistry.INSTANCE.register(EntityType.COW, VariantMobRenderer.Cow::new);

        if (VariantMobTextures.variantPigs)
            EntityRendererRegistry.INSTANCE.register(EntityType.PIG, VariantMobRenderer.Pig::new);

        if (VariantMobTextures.variantSheep)
            EntityRendererRegistry.INSTANCE.register(EntityType.SHEEP, VariantMobRenderer.Sheep::new);

        if (VariantMobTextures.variantSnowGolems)
            EntityRendererRegistry.INSTANCE.register(EntityType.SNOW_GOLEM, VariantMobRenderer.SnowGolem::new);

        if (VariantMobTextures.variantSquids)
            EntityRendererRegistry.INSTANCE.register(EntityType.SQUID, VariantMobRenderer.Squid::new);

        if (VariantMobTextures.variantWolves)
            EntityRendererRegistry.INSTANCE.register(EntityType.WOLF, VariantMobRenderer.Wolf::new);

    }
}
