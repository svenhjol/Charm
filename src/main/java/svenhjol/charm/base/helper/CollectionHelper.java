package svenhjol.charm.base.helper;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.collection.Pool;
import net.minecraft.util.collection.Weighted;
import net.minecraft.util.collection.Weighting;
import svenhjol.charm.mixin.accessor.PoolAccessor;

import java.util.ArrayList;
import java.util.List;

public class CollectionHelper {
    /**
     * Use this method directly when adding a pool entry.
     * Don't add an entry using the accessor unless you also set the pool entry weight.
     */
    public static <E extends Weighted> void addPoolEntry(Pool<E> pool, E entry) {
        PoolAccessor<E> p = (PoolAccessor) pool;
        List<E> entries = new ArrayList<>(p.getEntries());

        entries.add(entry);

        p.setEntries(ImmutableList.copyOf(entries));
        p.setTotalWeight(Weighting.getWeightSum(entries));
    }
}
