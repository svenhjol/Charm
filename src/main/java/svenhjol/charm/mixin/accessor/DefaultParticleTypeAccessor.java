package svenhjol.charm.mixin.accessor;

import net.minecraft.particle.DefaultParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.base.iface.CharmMixin;

@Mixin(DefaultParticleType.class)
@CharmMixin(required = true)
public interface DefaultParticleTypeAccessor {
    @Invoker("<init>")
    static DefaultParticleType invokeConstructor(boolean alwaysShow) {
        throw new IllegalStateException();
    }
}
