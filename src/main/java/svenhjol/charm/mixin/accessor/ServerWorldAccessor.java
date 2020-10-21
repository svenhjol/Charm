package svenhjol.charm.mixin.accessor;

import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerWorld.class)
public interface ServerWorldAccessor {
    @Invoker
    void callWakeSleepingPlayers();

    @Invoker
    void callResetWeather();
}
