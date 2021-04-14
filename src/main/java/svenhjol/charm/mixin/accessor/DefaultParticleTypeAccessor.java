package svenhjol.charm.mixin.accessor;

import net.minecraft.particle.DefaultParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(DefaultParticleType.class)
public interface DefaultParticleTypeAccessor {
    @Invoker("<init>")
    static DefaultParticleType invokeConstructor(boolean alwaysShow) {
        throw new IllegalStateException();
    }
}
