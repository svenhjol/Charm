package svenhjol.charm.feature.clear_item_frames.client;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;
import svenhjol.charm.feature.clear_item_frames.ClearItemFramesClient;
import svenhjol.charm.feature.clear_item_frames.common.Networking;
import svenhjol.charm.foundation.feature.RegisterHolder;

import java.util.function.Supplier;

public final class Registers extends RegisterHolder<ClearItemFramesClient> {
    public final Supplier<ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> particle;

    public Registers(ClearItemFramesClient feature) {
        super(feature);
        var registry = feature.registry();

        particle = registry.particle(feature().common.registers.particleType,
            () -> Particle::new);

        registry.packetReceiver(Networking.AddAmethyst.TYPE,
            () -> feature().handlers::addToItemFrame);
        registry.packetReceiver(Networking.RemoveAmethyst.TYPE,
            () -> feature().handlers::removeFromItemFrame);
    }
}
