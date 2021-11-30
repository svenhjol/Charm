package svenhjol.charm.mixin.event;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.event.GrindstoneEvents;

@Mixin(targets = {"net/minecraft/world/inventory/GrindstoneMenu$2", "net/minecraft/world/inventory/GrindstoneMenu$3"})
public class GrindstoneMenuInputMixin extends Slot {
    public GrindstoneMenuInputMixin(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @Inject(
        method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookMayPlace(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean result = GrindstoneEvents.CAN_PLACE.invoker().invoke(container, stack);
        if (result) {
            cir.setReturnValue(true);
        }
    }
}
