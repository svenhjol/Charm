package svenhjol.charm.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;
import java.util.Set;

@Mixin(PointOfInterestType.class)
@CharmMixin(required = true)
public interface PointOfInterestTypeAccessor {
    @Accessor
    void setTicketCount(int ticketCount);

    @Accessor
    Set<BlockState> getBlockStates();

    @Accessor
    void setBlockStates(Set<BlockState> states);

    @Accessor("REGISTERED_STATES")
    static Set<BlockState> getRegisteredStates() {
        throw new IllegalStateException();
    }

    @Accessor("BLOCK_STATE_TO_POINT_OF_INTEREST_TYPE")
    static Map<BlockState, PointOfInterestType> getBlockStateMap() {
        throw new IllegalStateException();
    }
}
