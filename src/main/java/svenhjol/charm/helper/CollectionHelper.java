package svenhjol.charm.helper;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.util.random.WeightedRandomList;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0.0-charm
 */
public class CollectionHelper {
    /**
     * Use this method directly when adding a pool entry.
     * Don't add an entry using the accessor unless you also set the pool entry weight.
     */
    public static <E extends WeightedEntry> void addPoolEntry(WeightedRandomList<E> pool, E entry) {
        List<E> entries = new ArrayList<>(pool.items);

        entries.add(entry);

        pool.items = ImmutableList.copyOf(entries);
        pool.totalWeight = WeightedRandom.getTotalWeight(entries);
    }
}
