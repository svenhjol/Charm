package svenhjol.charm.feature.clear_item_frames;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import svenhjol.charmony.base.CharmonyParticle;

@SuppressWarnings("ConstantConditions")
public class ApplyAmethystClientParticle implements ParticleProvider<SimpleParticleType> {
    private static final RandomSource RANDOM = RandomSource.create();
    private final SpriteSet spriteProvider;

    public ApplyAmethystClientParticle(SpriteSet spriteProvider) {
        this.spriteProvider = spriteProvider;
    }

    public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double r, double g, double b) {
        var particle = new CharmonyParticle(level, x, y, z,
            0.5D - RANDOM.nextDouble(), 0.5D - RANDOM.nextDouble(), 0.5D - RANDOM.nextDouble(), this.spriteProvider);
        particle.setLifetime(4 + RANDOM.nextInt(4));
        particle.setColor((float)r, (float)g, (float)b);
        particle.setAlpha((RANDOM.nextFloat() * 0.2F) + 0.8F);
        particle.friction = 0.8F; // some multiplier for velocity, idk
        particle.speedUpWhenYMotionIsBlocked = true; // idk
        return particle;
    }
}
