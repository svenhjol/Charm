package svenhjol.charm.mixin.gentle_potion_particles;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Redirect(
        method = "tickEffects",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V"
        )
    )
    private void hookTickStatusEffects(Level level, ParticleOptions parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        level.addParticle(ParticleTypes.AMBIENT_ENTITY_EFFECT, x, y, z, velocityX, velocityY, velocityZ);
    }
}
