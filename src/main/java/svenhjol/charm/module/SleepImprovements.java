package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import svenhjol.charm.mixin.accessor.ServerWorldAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import java.util.List;
import java.util.stream.Collectors;

@Module(description = "Allows the night to pass when a specified number of players are asleep.")
public class SleepImprovements extends MesonModule {
    @Config(name = "Number of required players", description = "The number of players required to sleep in order to bring the next day.")
    public static int requiredPlayers = 1;

    @Override
    public void init() {
        ServerTickEvents.END_WORLD_TICK.register((serverWorld -> {
            if (serverWorld != null
                && serverWorld.getTime() % 20 == 0
                && serverWorld.getRegistryKey().equals(World.OVERWORLD)
            ) {
                tryEndNight(serverWorld);
            }
        }));
    }

    private void tryEndNight(ServerWorld world) {
        MinecraftServer server = world.getServer();

        int currentPlayerCount = world.getServer().getCurrentPlayerCount();
        if (currentPlayerCount < requiredPlayers)
            return;

        List<ServerPlayerEntity> validPlayers = server.getPlayerManager().getPlayerList().stream()
            .filter(p -> !p.isSpectator() && p.isSleeping())
            .collect(Collectors.toList());

        if (validPlayers.size() < requiredPlayers)
            return;

        /** copypasta from {@link ServerWorld#tick} */
        if (world.getGameRules().getBoolean(GameRules.DO_DAYLIGHT_CYCLE)) {
            long l = world.getTimeOfDay() + 24000L;
            world.setTimeOfDay(l - l % 24000L);
        }

        ((ServerWorldAccessor)world).callWakeSleepingPlayers();
        if (world.getGameRules().getBoolean(GameRules.DO_WEATHER_CYCLE))
            ((ServerWorldAccessor)world).callResetWeather();
    }
}
