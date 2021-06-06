package svenhjol.charm.mixin.accessor;

import net.minecraft.core.particles.SimpleParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(SimpleParticleType.class)
@CharmMixin(required = true)
public interface SimpleParticleTypeAccessor {
    @Invoker("<init>")
    static SimpleParticleType invokeConstructor(boolean alwaysShow) {
        throw new IllegalStateException();
    }
}
