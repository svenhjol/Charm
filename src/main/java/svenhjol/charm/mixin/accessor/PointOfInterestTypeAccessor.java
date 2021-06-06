package svenhjol.charm.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;
import java.util.Set;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(PoiType.class)
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
    static Map<BlockState, PoiType> getBlockStateMap() {
        throw new IllegalStateException();
    }
}
