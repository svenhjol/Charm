package svenhjol.charm.event;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.structure.pool.LegacySinglePoolElement;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import svenhjol.charm.mixin.accessor.StructurePoolAccessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface StructureSetupCallback {
    Map<Identifier, StructurePool> vanillaPools = new HashMap<>();

    Event<StructureSetupCallback> EVENT = EventFactory.createArrayBacked(StructureSetupCallback.class, (listeners) -> () -> {
        for (StructureSetupCallback listener : listeners) {
            listener.interact();
        }
    });

    static StructurePool getVanillaPool(Identifier id) {
        if (!vanillaPools.containsKey(id)) {
            StructurePool pool = BuiltinRegistries.STRUCTURE_POOL.get(id);

            // convert elementCounts to mutable list
            List<Pair<StructurePoolElement, Integer>> elementCounts = ((StructurePoolAccessor) pool).getElementCounts();
            ((StructurePoolAccessor)pool).setElementCounts(new ArrayList<>(elementCounts));

            vanillaPools.put(id, pool);
        }

        return vanillaPools.get(id);
    }

    static void addStructurePoolElement(Identifier poolId, Identifier pieceId, StructureProcessorList processor, StructurePool.Projection projection, int count) {
        Pair<Function<StructurePool.Projection, LegacySinglePoolElement>, Integer> pair =
            Pair.of(StructurePoolElement.method_30426(pieceId.toString(), processor), count);

        StructurePoolElement element = pair.getFirst().apply(projection);
        StructurePool pool = getVanillaPool(poolId);

        if (false) { // DELETES ALL IN POOL, DO NOT USE!
            ((StructurePoolAccessor) pool).setElementCounts(new ArrayList<>());
        }
        
        // add custom piece to the element counts
        ((StructurePoolAccessor)pool).getElementCounts().add(Pair.of(element, count));
        
        // add custom piece to the elements
        for (int i = 0; i < count; i++) {
            ((StructurePoolAccessor)pool).getElements().add(element);
        }
    }

    void interact();
}
