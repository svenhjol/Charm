package svenhjol.charm.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import svenhjol.charm.helper.ClientHelper;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ColoredPortalParticle extends TextureSheetParticle {
    private static final Random RANDOM = new Random();

    private final double xStart;
    private final double yStart;
    private final double zStart;

    protected ColoredPortalParticle(ClientLevel clientLevel, double d, double e, double f, double g, double h, double i) {
        super(clientLevel, d, e, f);
        this.xd = g;
        this.yd = h;
        this.zd = i;
        this.x = d;
        this.y = e;
        this.z = f;
        this.xStart = this.x;
        this.yStart = this.y;
        this.zStart = this.z;
        this.quadSize = 0.1f * (this.random.nextFloat() * 0.2f + 0.5f);
        float j = this.random.nextFloat() * 0.6f + 0.4f;
        this.rCol = j * 0.9f;
        this.gCol = j * 0.3f;
        this.bCol = j;
        this.lifetime = (int)(Math.random() * 10.0) + 40;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double d, double e, double f) {
        this.setBoundingBox(this.getBoundingBox().move(d, e, f));
        this.setLocationFromBoundingbox();
    }

    @Override
    public float getQuadSize(float f) {
        float g = ((float)this.age + f) / (float)this.lifetime;
        g = 1.0f - g;
        g *= g;
        g = 1.0f - g;
        return this.quadSize * g;
    }

    @Override
    public int getLightColor(float f) {
        return 0xffffff;
    }

    @Override
    public void tick() {
        float f;
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }
        float g = f = (float)this.age / (float)this.lifetime;
        f = -f + f * f * 2.0f;
        f = 1.0f - f;
        this.x = this.xStart + this.xd * (double)f;
        this.y = this.yStart + this.yd * (double)f + (double)(1.0f - g);
        this.z = this.zStart + this.zd * (double)f;
    }

    @Environment(value = EnvType.CLIENT)
    public record Provider(SpriteSet spriteProvider) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double d, double f, double color) {
            var e = (RANDOM.nextDouble() - 0.5D) * 0.5D;
            double[] rgb = ClientHelper.getRgb((int)color);

            ColoredPortalParticle particle = new ColoredPortalParticle(level, x, y, z, d, e, f);
            particle.setColor((float)rgb[0], (float)rgb[1], (float)rgb[2]);
            particle.pickSprite(this.spriteProvider);
            return particle;
        }
    }
}
