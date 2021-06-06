package svenhjol.charm.mixin.accessor;

import com.mojang.datafixers.util.Pair;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.List;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;

@Mixin(StructureTemplatePool.class)
@CharmMixin(required = true)
public interface StructurePoolAccessor {
    @Accessor
    List<StructurePoolElement> getElements();

    @Accessor
    List<Pair<StructurePoolElement, Integer>> getElementCounts();

    @Accessor
    void setElementCounts(List<Pair<StructurePoolElement, Integer>> list);
}
