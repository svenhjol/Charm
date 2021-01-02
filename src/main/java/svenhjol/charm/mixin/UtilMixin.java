package svenhjol.charm.mixin;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.base.handler.RegistryHandler;

@Mixin(Util.class)
public class UtilMixin {
    @Inject(method = "getChoiceTypeInternal",
        at = @At(value = "INVOKE", target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Object;)V"),
        cancellable = true)
    private static void hookAttemptDataFixInternal(DSL.TypeReference typeIn, String choiceName, CallbackInfoReturnable<Type<?>> callbackInfo) {
        if(RegistryHandler.SUPPRESS_DATA_FIXER_ERROR.contains(choiceName)) {
            callbackInfo.setReturnValue(null);
        }
    }
}
