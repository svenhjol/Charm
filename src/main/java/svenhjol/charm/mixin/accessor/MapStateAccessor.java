package svenhjol.charm.mixin.accessor;

import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Map;

@Mixin(MapState.class)
@CharmMixin(required = true)
public interface MapStateAccessor {
    @Accessor
    Map<String, MapIcon> getIcons();

    @Invoker
    void invokeMarkDirty(int x, int y);
}
