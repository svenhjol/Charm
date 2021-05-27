package svenhjol.charm.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Pool.class)
public interface PoolAccessor<E extends Weighted> {
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
