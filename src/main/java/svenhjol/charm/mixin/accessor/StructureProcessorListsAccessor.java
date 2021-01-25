package svenhjol.charm.mixin.accessor;

import com.google.common.collect.ImmutableList;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorLists;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(StructureProcessorLists.class)
public interface StructureProcessorListsAccessor {
    @Accessor
    StructureProcessorList invokeRegister(String id, ImmutableList<StructureProcessor> processorList);
}
