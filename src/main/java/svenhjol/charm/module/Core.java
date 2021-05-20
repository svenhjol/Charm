package svenhjol.charm.module;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.CoreClient;
import svenhjol.charm.event.ServerJoinCallback;
import svenhjol.charm.init.CharmAdvancements;

@Module(mod = Charm.MOD_ID, priority = 100, client = CoreClient.class, alwaysEnabled = true, description = "Core configuration values.")
public class Core extends CharmModule {
    public static final Identifier ADVANCEMENT_PLAYER_JOINED = new Identifier(Charm.MOD_ID, "player_joined");
    public static final Identifier MSG_SERVER_OPEN_INVENTORY = new Identifier(Charm.MOD_ID, "server_open_inventory");

    @Config(name = "Debug mode", description = "If true, routes additional debug messages into the standard game log.")
    public static boolean debug = false;

    @Config(name = "Advancements", description = "If true, Charm will add its own advancement tree.")
    public static boolean advancements = true;

    @Override
    public void register() {
        ServerJoinCallback.EVENT.register(this::handleServerJoin);
    }

    private void handleServerJoin(PlayerManager playerManager, ClientConnection connection, ServerPlayerEntity player) {
        CharmAdvancements.ACTION_PERFORMED.trigger(player, ADVANCEMENT_PLAYER_JOINED);
    }
}
