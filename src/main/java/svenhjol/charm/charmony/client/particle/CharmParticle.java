package svenhjol.charm.charmony.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

@SuppressWarnings("unused")
public class CharmParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    /**
     * Copypasta
     * @see net.minecraft.client.particle.GlowParticle
     */
    public CharmParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz, SpriteSet spriteProvider) {
        super(level, x, y, z, vx, vy, vz);
        this.friction = 0.6f;
        this.speedUpWhenYMotionIsBlocked = false;
        this.spriteProvider = spriteProvider;
        this.quadSize *= 0.78f;
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteProvider);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    /**
     * Copypasta
     * @see net.minecraft.client.particle.PortalParticle
     */
    @Override
    public int getLightColor(float tint) {
        var i = super.getLightColor(tint);
        var f = (float) age / (float) lifetime;
        f *= f;
        f *= f;
        var j = i & 255;
        var k = i >> 16 & 255;
        k += (int) (f * 15.0f * 16.0f);
        if (k > 240) {
            k = 240;
        }

        return j | k << 16;
    }

    @Override
    public void tick() {
        super.tick();
        setSpriteFromAge(spriteProvider);
    }
}
