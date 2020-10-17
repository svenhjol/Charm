package svenhjol.charm.module;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.ServerWorldAccessor;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.List;
import java.util.stream.Collectors;

@Module(mod = Charm.MOD_ID, description = "Allows the night to pass when a specified number of players are asleep.")
public class SleepImprovements extends CharmModule {
    @Config(name = "Faster sleep", description = "If true, the sleeping player does not need to wait as long before ending the night.")
    public static boolean fasterSleep = false;

    @Config(name = "Number of required players", description = "The number of players required to sleep in order to bring the next day.")
    public static int requiredPlayers = 1;

    @Override
    public void init() {
        ServerTickEvents.END_WORLD_TICK.register(this::tryEndNight);
    }

    private void tryEndNight(ServerWorld world) {
        if (world == null || world.getTime() % 20 != 0 || !world.getRegistryKey().equals(World.OVERWORLD))
            return;

        MinecraftServer server = world.getServer();

        int currentPlayerCount = world.getServer().getCurrentPlayerCount();
        if (currentPlayerCount < requiredPlayers)
            return;

        List<ServerPlayerEntity> validPlayers = server.getPlayerManager().getPlayerList().stream()
            .filter(p -> !p.isSpectator() && (fasterSleep ? p.isSleeping() : p.isSleepingLongEnough()))
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
