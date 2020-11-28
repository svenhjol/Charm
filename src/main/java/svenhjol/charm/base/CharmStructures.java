package svenhjol.charm.base;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Blocks;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import svenhjol.charm.base.handler.DecorationHandler;
import svenhjol.charm.base.structure.DataBlockProcessor;

import java.util.Arrays;

public class CharmStructures {
    public static void init() {
        DecorationHandler.SINGLE_POOL_ELEMENT_PROCESSORS.addAll(Arrays.asList(
            new BlockIgnoreStructureProcessor(ImmutableList.of(Blocks.GRAY_STAINED_GLASS)),
            new DataBlockProcessor()
        ));
    }
}
