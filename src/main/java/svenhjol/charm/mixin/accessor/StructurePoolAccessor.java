package svenhjol.charm.mixin.accessor;

import com.mojang.datafixers.util.Pair;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.base.iface.CharmMixin;

import java.util.List;

@Mixin(StructurePool.class)
@CharmMixin(required = true)
public interface StructurePoolAccessor {
    @Accessor
    List<StructurePoolElement> getElements();

    @Accessor
    List<Pair<StructurePoolElement, Integer>> getElementCounts();

    @Accessor
    void setElementCounts(List<Pair<StructurePoolElement, Integer>> list);
}
