package svenhjol.charm.init;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.RegistryHelper;

public class CharmParticles {
    public static SimpleParticleType APPLY_PARTICLE;
    public static SimpleParticleType ORE_GLOW_PARTICLE;

    public static void init() {
        APPLY_PARTICLE = RegistryHelper.defaultParticleType(new ResourceLocation(Charm.MOD_ID, "apply"));
        ORE_GLOW_PARTICLE = RegistryHelper.defaultParticleType(new ResourceLocation(Charm.MOD_ID, "ore_glow"));
    }
}
