package svenhjol.charm.world;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public abstract class CharmGenerator {
    @Nullable
    protected static StructureTemplatePool registerPool(ResourceLocation startPool, List<svenhjol.charm.world.CharmStructure> structures, StructureTemplatePool.Projection projection) {
        if (structures.isEmpty())
            return emptyPool(startPool);

        // this is populated with starts for each custom ruin
        List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> starts = new ArrayList<>();

        // iterate over each custom structure, get all the start pools, and put them into the starts list
        structures.forEach(structure -> starts.addAll(structure.getStarts()));

        // return the start pool containing all the custom structure starts
        return Pools.register(
            new StructureTemplatePool(
                startPool,
                new ResourceLocation("empty"),
                ImmutableList.copyOf(starts),
                projection
            )
        );
    }

    @Nullable
    protected static StructureTemplatePool registerPool(ResourceLocation startPool, List<CharmStructure> strutures) {
        return registerPool(startPool, strutures, StructureTemplatePool.Projection.RIGID);
    }

    protected static StructureTemplatePool emptyPool(ResourceLocation poolName) {
        return new StructureTemplatePool(
            poolName,
            new ResourceLocation("empty"),
            ImmutableList.of(Pair.of(StructurePoolElement.empty(), 1)),
            StructureTemplatePool.Projection.RIGID
        );
    }
}
