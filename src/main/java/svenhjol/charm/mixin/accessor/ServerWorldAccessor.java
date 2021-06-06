package svenhjol.charm.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

import java.util.List;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.CustomSpawner;

@Mixin(ServerLevel.class)
@CharmMixin(required = true)
public interface ServerWorldAccessor {
    @Accessor
    List<CustomSpawner> getSpawners();

    @Invoker
    void callWakeSleepingPlayers();

    @Invoker
    void callResetWeather();
}
