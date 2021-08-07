package svenhjol.charm.event;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.LegacySinglePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import svenhjol.charm.mixin.accessor.StructureTemplatePoolAccessor;
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
            List<Pair<StructurePoolElement, Integer>> elementCounts = ((StructureTemplatePoolAccessor) pool).getRawTemplates();
            ((StructureTemplatePoolAccessor)pool).setRawTemplates(new ArrayList<>(elementCounts));

            if (false) { // DELETES ALL IN POOL, DO NOT USE!
                ((StructureTemplatePoolAccessor) pool).setRawTemplates(new ArrayList<>());
            }

            vanillaPools.put(id, pool);
        }

        return vanillaPools.get(id);
    }

    static void addStructurePoolElement(ResourceLocation poolId, ResourceLocation pieceId, StructureProcessorList processor, StructureTemplatePool.Projection projection, int count, Registry<StructureTemplatePool> poolRegistry) {
        Pair<Function<StructureTemplatePool.Projection, LegacySinglePoolElement>, Integer> pair =
            Pair.of(StructurePoolElement.legacy(pieceId.toString(), processor), count);

        StructurePoolElement element = pair.getFirst().apply(projection);
        StructureTemplatePool pool = poolRegistry.get(poolId);
        
        // add custom piece to the element counts
        List<Pair<StructurePoolElement, Integer>> listOfPieceEntries = new ArrayList<>(((StructureTemplatePoolAccessor)pool).getRawTemplates());
        listOfPieceEntries.add(new Pair<>(element, count));
        ((StructureTemplatePoolAccessor)pool).setRawTemplates(listOfPieceEntries);
        
        // add custom piece to the elements
        for (int i = 0; i < count; i++) {
            ((StructureTemplatePoolAccessor)pool).getTemplates().add(element);
        }
    }

    static void addVillageHouse(VillageType type, ResourceLocation pieceId, int count, List<StructureProcessor> charmProcessor, Registry<StructureTemplatePool> poolRegistry) {
        ResourceLocation houses = new ResourceLocation("village/" + type.getSerializedName() + "/houses");
        StructureTemplatePool.Projection projection = StructureTemplatePool.Projection.RIGID;
        addStructurePoolElement(houses, pieceId, new StructureProcessorList(charmProcessor), projection, count, poolRegistry);
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
