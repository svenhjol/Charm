package svenhjol.charm.world;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"SameParameterValue", "unused"})
public abstract class CharmStructure {
    private final String modId;
    private final String mainFolder;
    private final String structureName;
    private final List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> starts = new ArrayList<>();

    public CharmStructure(String modId, String mainFolder, String structureName) {
        this.modId = modId;
        this.mainFolder = mainFolder;
        this.structureName = structureName;
    }

    public List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> getStarts() {
        return starts;
    }

    protected void addStart(String pieceName, int weight) {
        addStart(pieceName, weight, ProcessorLists.EMPTY);
    }

    protected void addStart(String pieceName, int weight, StructureProcessorList processors) {
        starts.add(Pair.of(StructurePoolElement.single(getPiecePath(pieceName), processors), weight));
    }

    protected void registerPool(String poolName, Map<String, Integer> elements, StructureTemplatePool.Projection projection, StructureProcessorList processors) {
        final List<Pair<Function<StructureTemplatePool.Projection, ? extends StructurePoolElement>, Integer>> pieces = new ArrayList<>();

        elements.forEach((piece, weight) ->
            pieces.add(Pair.of(StructurePoolElement.single(getPiecePath(piece), processors), weight)));

        Pools.register(new StructureTemplatePool(
            getPoolPath(poolName),
            getPoolPath("ends"),
            ImmutableList.copyOf(pieces),
            projection
        ));
    }

    protected void registerPool(String poolName, Map<String, Integer> elements) {
        registerPool(poolName, elements, StructureTemplatePool.Projection.RIGID, ProcessorLists.EMPTY);
    }

    protected String getPiecePath(String piece) {
        return modId + ":" + mainFolder + "/" + structureName + "/" + piece;
    }

    protected ResourceLocation getPoolPath(String pool) {
        return new ResourceLocation(modId, mainFolder + "/" + structureName + "/" + pool);
    }
}
