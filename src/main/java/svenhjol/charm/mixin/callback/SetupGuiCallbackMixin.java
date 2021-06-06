package svenhjol.charm.mixin.callback;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.SetupGuiCallback;

import java.util.List;

@Mixin(Screen.class)
public abstract class SetupGuiCallbackMixin {
    @Shadow @Final
    private List<NarratableEntry> narratables;

    /**
     * Fires the {@link SetupGuiCallback} event.
     *
     * Simulates Forge's InitGuiEvent.Post.
     *
     * We can access existing GUI buttons and add new ones as required
     * using the SetupGuiCallback invoked in this hook.
     */
    @Inject(
        method = "init(Lnet/minecraft/client/Minecraft;II)V",
        at = @At("RETURN")
    )
    private void hookConstructor(Minecraft client, int width, int height, CallbackInfo ci) {
        SetupGuiCallback.EVENT.invoker().interact(client, width, height, this.narratables);
    }
}
