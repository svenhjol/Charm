package svenhjol.charm.mixin.gentle_potion_particles;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.gentle_potion_particles.GentlePotionParticles;

@Mixin(LivingEntity.class)
public class RenderTranslucentParticleMixin {
    /**
     * Defer to {@link GentlePotionParticles#tryRenderParticles}.
     * If the custom method returns false then no custom particles were rendered and so use the vanilla behavior.
     */
    @Redirect(
        method = "tickEffects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
        )
    )
    private void hookTickStatusEffects(Level level, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        boolean result = GentlePotionParticles.tryRenderParticles(level, x, y, z, velocityX, velocityY, velocityZ);
        if (!result) {
            level.addParticle(parameters, x, y, z, velocityX, velocityY, velocityZ); // vanilla behavior
        }
    }
}
