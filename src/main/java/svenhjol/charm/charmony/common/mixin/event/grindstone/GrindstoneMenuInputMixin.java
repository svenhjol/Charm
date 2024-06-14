package svenhjol.charm.charmony.common.mixin.event.grindstone;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.event.GrindstoneEvents;

@Mixin(targets = {
    "net/minecraft/world/inventory/GrindstoneMenu$2",
    "net/minecraft/world/inventory/GrindstoneMenu$3"
})
public class GrindstoneMenuInputMixin extends Slot {
    public GrindstoneMenuInputMixin(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
    }

    @ModifyReturnValue(
            method = "mayPlace(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("RETURN")
    )
    private boolean hookMayPlace(boolean original, @Local(argsOnly = true) ItemStack stack) {
        var result = GrindstoneEvents.CAN_PLACE.invoke(container, stack);
        if (result) {
            return true;
        }
        return original;
    }
}
