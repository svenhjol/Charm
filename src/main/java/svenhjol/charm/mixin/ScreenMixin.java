package svenhjol.charm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.RenderTooltipCallback;
import svenhjol.charm.event.GuiSetupCallback;

import java.util.List;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow
    @Final
    protected List<AbstractButtonWidget> buttons;

    protected ItemStack itemStack;

    @Shadow
    protected abstract <T extends AbstractButtonWidget> T addButton(T button);

    /**
     * Simulates Forge's InitGuiEvent.Post.
     * We can access existing GUI buttons and add new ones as required
     * using the SetupGuiCallback invoked in this hook.
     */
    @Inject(
        method = "init(Lnet/minecraft/client/MinecraftClient;II)V",
        at = @At("RETURN")
    )
    private void hookConstructor(MinecraftClient client, int width, int height, CallbackInfo ci) {
        GuiSetupCallback.EVENT.invoker().interact(client, width, height, this.buttons, this::addButton);
    }

    /**
     * Caches the ItemStack passed to getTooltipFromItem.
     * This is then passed to the RenderTooltipCallback event
     * in hookRenderOrderedTooltip.
     */
    @Inject(
        method = "getTooltipFromItem",
        at = @At("HEAD")
    )
    private void hookGetTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<Text>> cir) {
        itemStack = stack;
    }

    /**
     * Fires the RenderTooltipCallback event so that modules
     * can hook into the tooltip before it is rendered.
     */
    @Inject(
        method = "renderOrderedTooltip",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRenderOrderedTooltip(MatrixStack matrices, List<? extends OrderedText> lines, int x, int y, CallbackInfo ci) {
        ActionResult result = RenderTooltipCallback.EVENT.invoker().interact(matrices, itemStack, lines, x, y);
        if (result != ActionResult.PASS)
            ci.cancel();
    }
}
