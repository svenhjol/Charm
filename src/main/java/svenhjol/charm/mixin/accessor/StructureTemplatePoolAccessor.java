package svenhjol.charm.mixin.accessor;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(StructureTemplatePool.class)
public interface StructureTemplatePoolAccessor {
    @Accessor
    List<StructurePoolElement> getTemplates();

    @Accessor
    List<Pair<StructurePoolElement, Integer>> getRawTemplates();

    @Mutable @Accessor
    void setRawTemplates(List<Pair<StructurePoolElement, Integer>> list);
}
