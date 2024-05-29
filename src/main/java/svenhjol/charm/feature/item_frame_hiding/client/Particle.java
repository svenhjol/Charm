package svenhjol.charm.feature.item_frame_hiding.client;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import svenhjol.charm.charmony.client.particle.CharmParticle;

@SuppressWarnings("ConstantConditions")
public class Particle implements ParticleProvider<SimpleParticleType> {
    private static final RandomSource RANDOM = RandomSource.create();
    private final SpriteSet sprite;

    public Particle(SpriteSet sprite) {
        this.sprite = sprite;
    }

    public net.minecraft.client.particle.Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double r, double g, double b) {
        var particle = new CharmParticle(level, x, y, z,
            0.5d - RANDOM.nextDouble(), 0.5d - RANDOM.nextDouble(), 0.5d - RANDOM.nextDouble(), this.sprite);
        particle.setLifetime(10 + RANDOM.nextInt(10));
        particle.setColor((float)r, (float)g, (float)b);
        particle.setAlpha((RANDOM.nextFloat() * 0.2F) + 0.8F);
        particle.friction = 0.8F; // some multiplier for velocity, idk
        particle.speedUpWhenYMotionIsBlocked = true; // idk
        return particle;
    }
}
