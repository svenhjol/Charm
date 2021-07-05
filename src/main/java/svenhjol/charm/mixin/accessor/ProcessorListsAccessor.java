package svenhjol.charm.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ProcessorLists.class)
public interface ProcessorListsAccessor {
    @Invoker
    static StructureProcessorList invokeRegister(String id, ImmutableList<StructureProcessor> processorList) {
        throw new IllegalStateException();
    }
}
