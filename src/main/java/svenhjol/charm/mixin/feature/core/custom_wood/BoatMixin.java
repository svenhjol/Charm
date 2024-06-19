package svenhjol.charm.mixin.feature.core.custom_wood;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.core.custom_wood.CustomWood;

@Mixin(Boat.class)
public abstract class BoatMixin {
    @Shadow public abstract Boat.Type getVariant();

    @ModifyReturnValue(
        method = "getDropItem",
        at = @At("RETURN")
    )
    private Item getDropItem(Item original) {
        var opt = Resolve.feature(CustomWood.class).registers
            .getItemForBoat(getVariant());
        
        if (opt.isPresent()) {
            return opt.get();
        }
        
        return original;
    }
}
