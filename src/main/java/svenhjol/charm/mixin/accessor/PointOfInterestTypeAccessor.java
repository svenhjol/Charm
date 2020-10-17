package svenhjol.charm.mixin.accessor;

import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PointOfInterestType.class)
public interface PointOfInterestTypeAccessor {
    @Accessor
    void setTicketCount(int ticketCount);
}
