package svenhjol.charm.charmony.common.mixin.event.player_inventory_drop;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.event.PlayerInventoryDropEvent;

@SuppressWarnings("UnreachableCode")
@Mixin(Player.class)
public abstract class PlayerMixin {
    @Shadow @Final
    private Inventory inventory;

    /**
     * Fires the {@link PlayerInventoryDropEvent} event.
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
        InteractionResult result = PlayerInventoryDropEvent.INSTANCE.invoke((Player) (Object) this, this.inventory);
        if (result == InteractionResult.SUCCESS) {
            ci.cancel();
        }
    }
}
