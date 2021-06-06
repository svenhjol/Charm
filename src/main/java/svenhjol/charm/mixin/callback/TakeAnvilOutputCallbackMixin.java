package svenhjol.charm.mixin.callback;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.TakeAnvilOutputCallback;

@Mixin(AnvilMenu.class)
public class TakeAnvilOutputCallbackMixin {

    /**
     * Fires the {@link TakeAnvilOutputCallback} event when an item is taken from the anvil.
     * This event is useful for anvil-related advancements.
     */
    @Inject(
        method = "onTakeOutput",
        at = @At("HEAD")
    )
    private void hookOnTakeOutput(Player player, ItemStack stack, CallbackInfo ci) {
        TakeAnvilOutputCallback.EVENT.invoker().interact((AnvilMenu)(Object)this, player, stack);
    }
}
