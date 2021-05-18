package svenhjol.charm.mixin.callback;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.TakeAnvilOutputCallback;

@Mixin(AnvilScreenHandler.class)
public class TakeAnvilOutputCallbackMixin {

    /**
     * Fires the {@link TakeAnvilOutputCallback} event when an item is taken from the anvil.
     * This event is useful for anvil-related advancements.
     */
    @Inject(
        method = "onTakeOutput",
        at = @At("HEAD")
    )
    private void hookOnTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
        TakeAnvilOutputCallback.EVENT.invoker().interact((AnvilScreenHandler)(Object)this, player, stack);
    }
}
