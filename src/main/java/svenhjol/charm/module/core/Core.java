package svenhjol.charm.module.core;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.event.ServerJoinCallback;
import svenhjol.charm.init.CharmAdvancements;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, priority = 100, alwaysEnabled = true, description = "Core configuration values.")
public class Core extends CharmModule {
    public static final ResourceLocation ADVANCEMENT_PLAYER_JOINED = new ResourceLocation(Charm.MOD_ID, "player_joined");
    public static final ResourceLocation MSG_SERVER_OPEN_INVENTORY = new ResourceLocation(Charm.MOD_ID, "server_open_inventory");

    @Config(name = "Debug mode", description = "If true, routes additional debug messages into the standard game log.")
    public static boolean debug = false;

    @Config(name = "Use built-in biome hacks", description = "If true, uses Charm's biome hacks to add world features instead of Fabric's biome API.\nIt's very unlikely you want to enable this.")
    public static boolean useBiomeHacks = false;

    @Config(name = "Advancements", description = "If true, Charm will add its own advancement tree.")
    public static boolean advancements = true;

    @Override
    public void register() {
        ServerJoinCallback.EVENT.register(this::handleServerJoin);
    }

    private void handleServerJoin(PlayerList playerManager, Connection connection, ServerPlayer player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, ADVANCEMENT_PLAYER_JOINED);
    }

    public static boolean isDebugMode() {
        return debug || FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
