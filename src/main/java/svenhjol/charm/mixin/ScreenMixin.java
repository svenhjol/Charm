package svenhjol.charm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.SetupGuiCallback;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow
    @Final
    protected List<AbstractButtonWidget> buttons;

    @Shadow
    protected abstract <T extends AbstractButtonWidget> T addButton(T button);

    /**
     * Simulates Forge's InitGuiEvent.Post
     */
    @Inject(method = "init(Lnet/minecraft/client/MinecraftClient;II)V", at = @At("RETURN"))
    private void hookConstructor(MinecraftClient client, int width, int height, CallbackInfo ci) {
        SetupGuiCallback.EVENT.invoker().interact(client, width, height, this.buttons, this::addButton);
    }
}
