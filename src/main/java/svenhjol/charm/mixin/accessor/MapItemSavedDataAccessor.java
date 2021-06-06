package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;

@Mixin(MapItemSavedData.class)
@CharmMixin(required = true)
public interface MapItemSavedDataAccessor {
    @Accessor
    Map<String, MapDecoration> getDecorations();

    @Invoker
    void invokeSetColorsDirty(int x, int y);
}
