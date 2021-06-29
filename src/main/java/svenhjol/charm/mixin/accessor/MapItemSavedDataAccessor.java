package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(MapItemSavedData.class)
public interface MapItemSavedDataAccessor {
    @Accessor
    Map<String, MapDecoration> getDecorations();

    @Invoker
    void invokeSetColorsDirty(int x, int y);
}
