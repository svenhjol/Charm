package svenhjol.charm.mixin.no_experimental_screen;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldOpenFlows.class)
public abstract class WorldOpenFlowsMixin {
    @Shadow protected abstract void doLoadLevel(Screen screen, String string, boolean bl, boolean bl2);

    @Unique
    private boolean skippedDialog;

    @Redirect(
        method = "doLoadLevel*",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows;askForBackup(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;ZLjava/lang/Runnable;)V"
        )
    )
    private void hookDisplayDialog(WorldOpenFlows instance, Screen screen, String string, boolean bl, Runnable runnable) {
        skippedDialog = true;
    }

    @Inject(
        method = "doLoadLevel*",
        at = @At("RETURN")
    )
    private void hookForceLoadLevel(Screen screen, String string, boolean bl, boolean bl2, CallbackInfo ci) {
        if (skippedDialog) {
            skippedDialog = false;
            doLoadLevel(screen, string, bl, false);
        }
    }
}
