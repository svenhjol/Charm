package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import svenhjol.charm.base.CharmClientLoader;
import svenhjol.charm.base.CharmParticles;
import svenhjol.charm.base.handler.LogHandler;
import svenhjol.charm.base.particle.GlowParticle;

public class CharmClient implements ClientModInitializer {
    public static LogHandler LOG = new LogHandler("CharmClient");

    @Override
    public void onInitializeClient() {
        new CharmClientLoader(Charm.MOD_ID);

        // TODO: static init for particles
        ParticleFactoryRegistry.getInstance().register(CharmParticles.AXIS_PARTICLE, GlowParticle.AxisFactory::new);
    }
}
