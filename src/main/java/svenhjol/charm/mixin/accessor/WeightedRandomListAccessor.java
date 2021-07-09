package svenhjol.charm.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(WeightedRandomList.class)
public interface WeightedRandomListAccessor<E extends WeightedEntry> {
    @Mutable @Accessor
    ImmutableList<E> getItems();

    @Mutable @Accessor
    void setItems(ImmutableList<E> items);

    @Mutable @Accessor
    void setTotalWeight(int totalWeight);
}
