package svenhjol.charm.mixin.gentle_potion_particles;

import net.minecraft.entity.LivingEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.gentle_potion_particles.GentlePotionParticles;

@Mixin(LivingEntity.class)
public class RenderTranslucentParticleMixin {

    /**
     * Defer to tryRenderParticles.
     *
     * If the custom method returns false then no custom particles
     * were rendered and so use the vanilla behavior.
     */
    @Redirect(
        method = "tickStatusEffects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"
        )
    )
    private void hookTickStatusEffects(World world, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        boolean result = GentlePotionParticles.tryRenderParticles(world, x, y, z, velocityX, velocityY, velocityZ);
        if (!result)
            world.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ); // vanilla behavior
    }
}
