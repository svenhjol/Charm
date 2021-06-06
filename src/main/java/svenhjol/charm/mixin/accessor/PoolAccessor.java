package svenhjol.charm.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WeightedRandomList.class)
public interface PoolAccessor<E extends WeightedEntry> {
    @Mutable
    @Accessor
    ImmutableList<E> getEntries();

    @Mutable
    @Accessor
    void setEntries(ImmutableList<E> entries);

    @Mutable
    @Accessor
    int getTotalWeight();

    @Mutable
    @Accessor
    void setTotalWeight(int totalWeight);
}
