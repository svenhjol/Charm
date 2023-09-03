package svenhjol.charm.mixin.wood;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.wood.Wood;

@Mixin(Boat.class)
public abstract class BoatMixin {
    @Shadow
    public abstract Boat.Type getVariant();

    @Inject(
        method = "getDropItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetDropItem(CallbackInfoReturnable<Item> cir) {
        var boatByType = Wood.getBoatByType(getVariant());
        if (boatByType != null ){
            cir.setReturnValue(boatByType);
        }
    }
}
