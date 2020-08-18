package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.entity.EntityType;
import svenhjol.charm.render.VariantChickenRenderer;
import svenhjol.meson.MesonModule;

public class VariantAnimalTexturesClient {
    public VariantAnimalTexturesClient(MesonModule module) {
        EntityRendererRegistry.INSTANCE.register(EntityType.CHICKEN, ((dispatcher, context) -> new VariantChickenRenderer(dispatcher)));
    }
}
