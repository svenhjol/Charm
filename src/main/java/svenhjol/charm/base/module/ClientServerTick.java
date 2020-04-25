package svenhjol.charm.base.module;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.base.message.ServerUpdatePlayerState;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.CORE, hasSubscriptions = true,
    description = "Client to server communication options.\nPeriodically sends extra world state from server to client.")
public class ClientServerTick extends MesonModule {
    @Config(name = "Client/Server update ticks", description = "Heartbeat interval to synchronise additional world state to the client.")
    public static int clientServerUpdateTicks = 120;

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END
            && event.player.world.isRemote
            && event.player.world.getGameTime() % clientServerUpdateTicks == 0
        ) {
            Meson.getInstance(Charm.MOD_ID).getPacketHandler().sendToServer(new ServerUpdatePlayerState());
        }
    }
}
