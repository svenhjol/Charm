package svenhjol.charm.mixin.accessor;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(NativeImage.class)
@CharmMixin(required = true)
public interface NativeImageAccessor {
    @Invoker("<init>")
    static NativeImage invokeConstructor(NativeImage.Format format, int width, int height, boolean useStb, long pointer) {
        throw new IllegalStateException();
    }
}
