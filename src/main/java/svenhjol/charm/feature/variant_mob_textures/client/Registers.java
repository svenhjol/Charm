package svenhjol.charm.feature.variant_mob_textures.client;

import net.minecraft.world.entity.EntityType;
import svenhjol.charm.api.event.ClientEntityJoinEvent;
import svenhjol.charm.feature.variant_mob_textures.VariantMobTextures;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<VariantMobTextures> {
    public Registers(VariantMobTextures feature) {
        super(feature);
        var registry = feature.registry();

        if (VariantMobTextures.chickens) {
            registry.entityRenderer(() -> EntityType.CHICKEN,
                () -> Renderer.RenderChicken::new);
        }

        if (VariantMobTextures.cows) {
            registry.entityRenderer(() -> EntityType.COW,
                () -> Renderer.RenderCow::new);
        }

        if (VariantMobTextures.dolphins) {
            registry.entityRenderer(() -> EntityType.DOLPHIN,
                () -> Renderer.RenderDolphin::new);
        }

        if (VariantMobTextures.pigs) {
            registry.entityRenderer(() -> EntityType.PIG,
                () -> Renderer.RenderPig::new);
        }

        if (VariantMobTextures.sheep) {
            registry.entityRenderer(() -> EntityType.SHEEP,
                () -> Renderer.RenderSheep::new);
        }

        if (VariantMobTextures.snowGolems) {
            registry.entityRenderer(() -> EntityType.SNOW_GOLEM,
                () -> Renderer.RenderSnowGolem::new);
        }

        if (VariantMobTextures.squids) {
            registry.entityRenderer(() -> EntityType.SQUID,
                () -> Renderer.RenderSquid::new);
        }

        if (VariantMobTextures.turtles) {
            registry.entityRenderer(() -> EntityType.TURTLE,
                () -> Renderer.RenderTurtle::new);
        }

        if (VariantMobTextures.wanderingTraders) {
            registry.entityRenderer(() -> EntityType.WANDERING_TRADER,
                () -> Renderer.RenderWanderingTrader::new);
        }
    }

    @Override
    public void onEnabled() {
        ClientEntityJoinEvent.INSTANCE.handle(feature().handlers::playerJoin);
    }
}
