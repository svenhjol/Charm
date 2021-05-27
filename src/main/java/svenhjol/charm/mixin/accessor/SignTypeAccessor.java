package svenhjol.charm.mixin.accessor;

import net.minecraft.util.SignType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(SignType.class)
@CharmMixin(required = true)
public interface SignTypeAccessor {
    @Invoker("<init>")
    static SignType invokeInit(String name) {
        throw new IllegalStateException();
    }

    @Invoker
    static SignType invokeRegister(SignType type) {
        throw new IllegalStateException();
    }
}
