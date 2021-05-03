package svenhjol.charm.init;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import svenhjol.charm.particle.GlowParticle;

public class CharmClientParticles {
    public static void init() {
        ParticleFactoryRegistry.getInstance().register(CharmParticles.APPLY_PARTICLE, GlowParticle.ApplyFactory::new);
        ParticleFactoryRegistry.getInstance().register(CharmParticles.AXIS_PARTICLE, GlowParticle.AxisFactory::new);
        ParticleFactoryRegistry.getInstance().register(CharmParticles.ORE_GLOW_PARTICLE, GlowParticle.OreGlowFactory::new);
    }
}
