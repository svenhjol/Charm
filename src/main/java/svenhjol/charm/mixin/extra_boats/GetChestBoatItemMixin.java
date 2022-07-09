package svenhjol.charm.mixin.extra_boats;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.extra_boats.ExtraBoats;

@Mixin(ChestBoat.class)
public abstract class GetChestBoatItemMixin extends Boat {
    public GetChestBoatItemMixin(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "getDropItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetDropItem(CallbackInfoReturnable<Item> cir) {
        var boatByType = ExtraBoats.getChestBoatByType(getBoatType());
        if (boatByType != null ){
            cir.setReturnValue(boatByType);
        }
    }
}
