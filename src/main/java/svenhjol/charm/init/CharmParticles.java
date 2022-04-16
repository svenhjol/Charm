package svenhjol.charm.init;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.registry.CommonRegistry;

public class CharmParticles {
    public static SimpleParticleType APPLY_PARTICLE;
    public static SimpleParticleType ORE_GLOW_PARTICLE;
    public static SimpleParticleType COLORED_PORTAL_PARTICLE;

    public static void init() {
        APPLY_PARTICLE = CommonRegistry.defaultParticleType(new ResourceLocation(Charm.MOD_ID, "apply"));
        COLORED_PORTAL_PARTICLE = CommonRegistry.defaultParticleType(new ResourceLocation(Charm.MOD_ID, "colored_portal"));
        ORE_GLOW_PARTICLE = CommonRegistry.defaultParticleType(new ResourceLocation(Charm.MOD_ID, "ore_glow"));
    }
}
