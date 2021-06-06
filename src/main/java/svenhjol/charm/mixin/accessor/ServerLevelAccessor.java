package svenhjol.charm.mixin.accessor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

import java.util.List;

@Mixin(ServerLevel.class)
@CharmMixin(required = true)
public interface ServerLevelAccessor {
    @Accessor
    List<CustomSpawner> getCustomSpawners();

    @Invoker
    void invokeWakeUpAllPlayers();

    @Invoker
    void invokeStopWeather();
}
