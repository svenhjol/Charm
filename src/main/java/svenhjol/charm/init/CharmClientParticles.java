package svenhjol.charm.init;

import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import svenhjol.charm.client.CharmParticle;

public class CharmClientParticles {
    public static void init() {
        ParticleFactoryRegistry.getInstance().register(CharmParticles.APPLY_PARTICLE, CharmParticle.ApplyFactory::new);
        ParticleFactoryRegistry.getInstance().register(CharmParticles.AXIS_PARTICLE, CharmParticle.AxisFactory::new);
        ParticleFactoryRegistry.getInstance().register(CharmParticles.ORE_GLOW_PARTICLE, CharmParticle.OreGlowFactory::new);
    }
}
