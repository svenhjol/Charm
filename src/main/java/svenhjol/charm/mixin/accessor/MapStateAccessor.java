package svenhjol.charm.mixin.accessor;

import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(MapState.class)
public interface MapStateAccessor {
    @Accessor
    Map<String, MapIcon> getIcons();

    @Invoker
    void invokeMarkDirty(int x, int y);
}
