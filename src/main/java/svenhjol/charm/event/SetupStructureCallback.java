package svenhjol.charm.event;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import svenhjol.charm.mixin.accessor.StructurePoolAccessor;
import svenhjol.charm.enums.ICharmEnum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface SetupStructureCallback {
    Map<ResourceLocation, StructureTemplatePool> vanillaPools = new HashMap<>();

    Event<SetupStructureCallback> EVENT = EventFactory.createArrayBacked(SetupStructureCallback.class, (listeners) -> () -> {
        for (SetupStructureCallback listener : listeners) {
            listener.interact();
        }
    });

    static StructureTemplatePool getVanillaPool(ResourceLocation id) {
        if (!vanillaPools.containsKey(id)) {
            StructureTemplatePool pool = BuiltinRegistries.TEMPLATE_POOL.get(id);

            // convert elementCounts to mutable list
            List<Pair<StructurePoolElement, Integer>> elementCounts = ((StructurePoolAccessor) pool).getElementCounts();
            ((StructurePoolAccessor)pool).setElementCounts(new ArrayList<>(elementCounts));

            if (false) { // DELETES ALL IN POOL, DO NOT USE!
                ((StructurePoolAccessor) pool).setElementCounts(new ArrayList<>());
            }

            vanillaPools.put(id, pool);
        }

        return vanillaPools.get(id);
    }

    static void addStructurePoolElement(ResourceLocation poolId, ResourceLocation pieceId, StructureProcessorList processor, StructureTemplatePool.Projection projection, int count) {
        Pair<Function<StructureTemplatePool.Projection, LegacySinglePoolElement>, Integer> pair =
            Pair.of(StructurePoolElement.legacy(pieceId.toString(), processor), count);

        StructurePoolElement element = pair.getFirst().apply(projection);
        StructureTemplatePool pool = getVanillaPool(poolId);
        
        // add custom piece to the element counts
        ((StructurePoolAccessor)pool).getElementCounts().add(Pair.of(element, count));
        
        // add custom piece to the elements
        for (int i = 0; i < count; i++) {
            ((StructurePoolAccessor)pool).getElements().add(element);
        }
    }

    static void addVillageHouse(VillageType type, ResourceLocation pieceId, int count) {
        ResourceLocation houses = new ResourceLocation("village/" + type.getSerializedName() + "/houses");
        StructureProcessorList processor = ProcessorLists.MOSSIFY_10_PERCENT;
        StructureTemplatePool.Projection projection = StructureTemplatePool.Projection.RIGID;
        addStructurePoolElement(houses, pieceId, processor, projection, count);
    }

    void interact();

    enum VillageType implements ICharmEnum {
        DESERT,
        PLAINS,
        SAVANNA,
        SNOWY,
        TAIGA
    }
}
