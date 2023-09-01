package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MapItemSavedData.class)
public interface MapItemSavedDataAccessor {
    @Invoker()
    void invokeSetColorsDirty(int i, int j);
}
