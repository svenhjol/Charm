package svenhjol.charm.mixin.callback;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.RenderTooltipCallback;

import java.util.List;

@Mixin(Screen.class)
public class RenderTooltipCallbackMixin {

    protected ItemStack itemStack;

    /**
     * Fires the {@link RenderTooltipCallback} event.
     * Modules can hook into the tooltip before it is rendered.
     */
    @Inject(
        method = "renderTooltipFromComponents",
        at = @At("HEAD")
    )
    private void hookRenderOrderedTooltip(MatrixStack matrices, List<TooltipComponent> lines, int x, int y, CallbackInfo ci) {
        RenderTooltipCallback.EVENT.invoker().interact(matrices, itemStack, lines, x, y);
        itemStack = null;
    }

    /**
     * Caches the ItemStack passed to getTooltipFromItem.
     * This is then passed to the RenderTooltipCallback event above.
     */
    @Inject(
        method = "getTooltipFromItem",
        at = @At("HEAD")
    )
    private void hookGetTooltipFromItem(ItemStack stack, CallbackInfoReturnable<List<Text>> cir) {
        itemStack = stack;
    }
}
