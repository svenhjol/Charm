package svenhjol.charm.mixin.feature.core.custom_wood;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.core.custom_wood.CustomWood;

@Mixin(ChestBoat.class)
public abstract class ChestBoatMixin extends Boat {
    public ChestBoatMixin(EntityType<? extends Boat> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyReturnValue(
        method = "getDropItem",
        at = @At("RETURN")
    )
    private Item getDropItem(Item original) {
        var opt = Resolve.feature(CustomWood.class).registers
            .getItemForChestBoat(getVariant());
        
        if (opt.isPresent()) {
            return opt.get();
        }
        
        return original;
    }
}
