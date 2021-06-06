package svenhjol.charm.mixin.accessor;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;
import java.util.Set;

@Mixin(PoiType.class)
@CharmMixin(required = true)
public interface PoiTypeAccessor {
    @Mutable @Accessor
    void setMaxTickets(int maxTickets);

    @Accessor
    Set<BlockState> getMatchingStates();

    @Mutable @Accessor
    void setMatchingStates(Set<BlockState> states);

    @Accessor("ALL_STATES")
    static Set<BlockState> getAllStates() {
        throw new IllegalStateException();
    }

    @Accessor("TYPE_BY_STATE")
    static Map<BlockState, PoiType> getTypeByState() {
        throw new IllegalStateException();
    }
}
