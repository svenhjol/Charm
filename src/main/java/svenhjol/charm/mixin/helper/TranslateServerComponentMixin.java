package svenhjol.charm.mixin.helper;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.helper.StringHelper;

@Mixin(MinecraftServer.class)
public class TranslateServerComponentMixin {
    @Shadow @Final
    private static Logger LOGGER;

    /**
     * For non-vanilla items and advancements, etc., Minecraft will always create
     * TextComponent of the language key instead of using the language key's
     * translated value.
     *
     * Try and intercept the text component with the contents of an en_us string
     * so that it gets saved to the server log correctly.
     */
    @Inject(
        method = "sendSystemMessage",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookSendMessage(Component component, CallbackInfo ci) {
        StringHelper.tryTranslateServerComponent(component)
            .ifPresent(translated -> {
                LOGGER.info(translated);
                ci.cancel();
            });
    }
}
