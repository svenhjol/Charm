package svenhjol.charm.mixin.wood;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.wood.Wood;

@Mixin(ChestBoat.class)
public abstract class ChestBoatMixin extends Boat {
    public ChestBoatMixin(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "getDropItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetDropItem(CallbackInfoReturnable<Item> cir) {
        var boatByType = Wood.getChestBoatByType(getVariant());
        if (boatByType != null ){
            cir.setReturnValue(boatByType);
        }
    }
}
