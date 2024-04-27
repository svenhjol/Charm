package svenhjol.charm.foundation.deferred;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.SimpleParticleType;

import java.util.function.Supplier;

public record DeferredParticle(
    Supplier<SimpleParticleType> particleType,
    Supplier<ParticleEngine.SpriteParticleRegistration<SimpleParticleType>> particleProvider
) {}
