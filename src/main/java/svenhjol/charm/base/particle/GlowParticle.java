package svenhjol.charm.base.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class GlowParticle extends SpriteBillboardParticle {
    private static final Random RANDOM = new Random();
    private final SpriteProvider spriteProvider;

    // copypasta from GlowParticle#<init>
    public GlowParticle(ClientWorld world, double x, double y, double z, double vx, double vy, double vz, SpriteProvider spriteProvider) {
        super(world, x, y, z, vx, vy, vz);
        this.field_28786 = 0.6F;
        this.field_28787 = false;
        this.spriteProvider = spriteProvider;
        this.scale *= 0.78F;
        this.collidesWithWorld = false;
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    // copypasta from PortalParticle#getBrightness
    @Override
    public int getBrightness(float tint) {
        int i = super.getBrightness(tint);
        float f = (float) this.age / (float) this.maxAge;
        f *= f;
        f *= f;
        int j = i & 255;
        int k = i >> 16 & 255;
        k += (int) (f * 15.0F * 16.0F);
        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    // copypasta from GlowParticle#tick
    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }

    public static class AxisFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public AxisFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double r, double g, double b) {
            GlowParticle particle = new GlowParticle(world, x, y, z, 0.5D, 0.5D, 0.5D, this.spriteProvider);
            particle.setMaxAge(80 + RANDOM.nextInt(20));
            particle.setColor((float) r, (float) g, (float) b);
            particle.setColorAlpha(0.81F);
            particle.field_28786 = 0.1F; // some multiplier for velocity, idk
            particle.field_28787 = true;
            return particle;
        }
    }

    public static class OreGlowFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public OreGlowFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double r, double g, double b) {
            GlowParticle particle = new GlowParticle(world, x, y, z, 0.06D, 0.6D - RANDOM.nextDouble(), 0.05D, this.spriteProvider);
            particle.setMaxAge(25 + RANDOM.nextInt(2));
            particle.setColor((float)r, (float)g, (float)b);
            particle.setColorAlpha((RANDOM.nextFloat() * 0.2F) + 0.8F);
            particle.field_28786 = 0.7F; // some multiplier for velocity, idk
            particle.field_28787 = true;
            return particle;
        }
    }

    public static class ApplyFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public ApplyFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double r, double g, double b) {
            GlowParticle particle = new GlowParticle(world, x, y, z, 0.5D - RANDOM.nextDouble(), 0.5D - RANDOM.nextDouble(), 0.5D - RANDOM.nextDouble(), this.spriteProvider);
            particle.setMaxAge(4 + RANDOM.nextInt(4));
            particle.setColor((float)r, (float)g, (float)b);
            particle.setColorAlpha((RANDOM.nextFloat() * 0.2F) + 0.8F);
            particle.field_28786 = 0.8F; // some multiplier for velocity, idk
            particle.field_28787 = true; // dunno
            return particle;
        }
    }
}
