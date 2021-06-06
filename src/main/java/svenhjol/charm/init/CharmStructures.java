package svenhjol.charm.init;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import svenhjol.charm.Charm;
import svenhjol.charm.init.CharmDecoration;
import svenhjol.charm.structure.CharmDataBlockProcessor;

import java.util.Arrays;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

public class CharmStructures {
    private static final CharmDataBlockProcessor DATA_BLOCK_PROCESSOR = new CharmDataBlockProcessor();
    private static final Codec<CharmDataBlockProcessor> CODEC = Codec.unit(DATA_BLOCK_PROCESSOR);
    public static final StructureProcessorType<CharmDataBlockProcessor> DATA_BLOCK_PROCESSOR_TYPE = () -> CODEC;

    public static void init() {
        CharmDecoration.SINGLE_POOL_ELEMENT_PROCESSORS.addAll(Arrays.asList(
            new BlockIgnoreProcessor(ImmutableList.of(Blocks.GRAY_STAINED_GLASS)),
            DATA_BLOCK_PROCESSOR
        ));
        Registry.register(Registry.STRUCTURE_PROCESSOR, new ResourceLocation(Charm.MOD_ID, "data_block_processor"), DATA_BLOCK_PROCESSOR_TYPE);
    }
}
