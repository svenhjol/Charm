package svenhjol.charm.mixin.feature.silence;

import com.mojang.serialization.Lifecycle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.feature.silence.Silence;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {
    @Inject(
        method = "confirmWorldCreation",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookConfirmWorldCreation(Minecraft minecraft, CreateWorldScreen createWorldScreen, Lifecycle lifecycle, Runnable runnable, boolean bl, CallbackInfo ci) {
        if (Silence.disableExperimental && lifecycle == Lifecycle.experimental()) {
            runnable.run();
            ci.cancel();
        }
    }
}
