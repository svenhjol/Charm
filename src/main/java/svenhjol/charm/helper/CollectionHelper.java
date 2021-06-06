package svenhjol.charm.helper;

import com.google.common.collect.ImmutableList;
import svenhjol.charm.mixin.accessor.PoolAccessor;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.util.random.WeightedRandomList;

public class CollectionHelper {
    /**
     * Use this method directly when adding a pool entry.
     * Don't add an entry using the accessor unless you also set the pool entry weight.
     */
    public static <E extends WeightedEntry> void addPoolEntry(WeightedRandomList<E> pool, E entry) {
        PoolAccessor<E> p = (PoolAccessor) pool;
        List<E> entries = new ArrayList<>(p.getEntries());

        entries.add(entry);

        p.setEntries(ImmutableList.copyOf(entries));
        p.setTotalWeight(WeightedRandom.getTotalWeight(entries));
    }
}
