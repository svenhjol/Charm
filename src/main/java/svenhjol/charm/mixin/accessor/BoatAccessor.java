package svenhjol.charm.mixin.accessor;

import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(Boat.class)
@CharmMixin(required = true)
public interface BoatAccessor {
    @Mutable @Accessor
    void setPaddlePositions(float[] positions);
}
