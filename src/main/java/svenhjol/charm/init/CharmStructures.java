package svenhjol.charm.init;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.StructureProcessorType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import svenhjol.charm.Charm;
import svenhjol.charm.structure.CharmDataBlockProcessor;

import java.util.Arrays;

public class CharmStructures {
    private static final CharmDataBlockProcessor DATA_BLOCK_PROCESSOR = new CharmDataBlockProcessor();
    private static final Codec<CharmDataBlockProcessor> CODEC = Codec.unit(DATA_BLOCK_PROCESSOR);
    public static final StructureProcessorType<CharmDataBlockProcessor> DATA_BLOCK_PROCESSOR_TYPE = () -> CODEC;

    public static void init() {
        CharmDecoration.SINGLE_POOL_ELEMENT_PROCESSORS.addAll(Arrays.asList(
            new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.GRAY_STAINED_GLASS)),
            DATA_BLOCK_PROCESSOR
        ));
        Registry.register(Registry.STRUCTURE_PROCESSOR, new Identifier(Charm.MOD_ID, "data_block_processor"), DATA_BLOCK_PROCESSOR_TYPE);
    }
}
