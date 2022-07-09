package svenhjol.charm.mixin.extra_boats;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.extra_boats.ExtraBoats;

@Mixin(Boat.class)
public abstract class GetBoatItemMixin {
    @Shadow public abstract Boat.Type getBoatType();

    @Inject(
        method = "getDropItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetDropItem(CallbackInfoReturnable<Item> cir) {
        var boatByType = ExtraBoats.getBoatByType(getBoatType());
        if (boatByType != null ){
            cir.setReturnValue(boatByType);
        }
    }
}
