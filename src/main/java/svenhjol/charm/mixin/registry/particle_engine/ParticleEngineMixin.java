package svenhjol.charm.mixin.registry.particle_engine;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.foundation.client.ClientRegistry;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {
    @Shadow
    protected abstract <T extends ParticleOptions> void register(ParticleType<T> particleType, ParticleEngine.SpriteParticleRegistration<T> spriteParticleRegistration);

    @Inject(
        method = "registerProviders",
        at = @At("RETURN")
    )
    private void hookRegisterProviders(CallbackInfo ci) {
        var particles = ClientRegistry.particles();

        for (var deferred : particles) {
            var type = deferred.particleType().get();
            var provider = deferred.particleProvider().get();
            register(type, provider);
        }
    }
}
