package svenhjol.charm.mixin.event;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.PlayerDropInventoryEvent;

@Mixin(Player.class)
public abstract class PlayerDropInventoryCallbackMixin {
    @Shadow @Final
    private Inventory inventory;

    /**
     * Fires the {@link PlayerDropInventoryEvent} event.
     *
     * Cancellable with ActionResult == SUCCESS.
     */
    @Inject(
        method = "dropEquipment",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;dropAll()V"
        ),
        cancellable = true
    )
    private void hookDropInventory(CallbackInfo ci) {
        InteractionResult result = PlayerDropInventoryEvent.EVENT.invoker().interact((Player) (Object) this, this.inventory);
        if (result == InteractionResult.SUCCESS)
            ci.cancel();
    }
}
