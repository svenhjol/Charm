package svenhjol.charm.mixin.callback;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.PlayerDropInventoryCallback;

@Mixin(PlayerEntity.class)
public abstract class PlayerDropInventoryCallbackMixin {

    @Shadow
    @Final
    private PlayerInventory inventory;

    /**
     * Fires the {@link PlayerDropInventoryCallback} event.
     *
     * Cancellable with ActionResult == SUCCESS.
     */
    @Inject(
        method = "dropInventory",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/player/PlayerInventory;dropAll()V"
        ),
        cancellable = true
    )
    private void hookDropInventory(CallbackInfo ci) {
        ActionResult result = PlayerDropInventoryCallback.EVENT.invoker().interact((PlayerEntity) (Object) this, this.inventory);
        if (result == ActionResult.SUCCESS)
            ci.cancel();
    }
}
