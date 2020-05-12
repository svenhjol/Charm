package svenhjol.charm;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.base.CharmClient;
import svenhjol.charm.base.CharmCompat;
import svenhjol.charm.base.CharmMessages;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.base.compat.IQuarkCompat;
import svenhjol.meson.MesonInstance;
import svenhjol.meson.handler.LogHandler;

@Mod(Charm.MOD_ID)
public class Charm extends MesonInstance {
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler(Charm.MOD_ID);
    public static IQuarkCompat quarkCompat;
    public static CharmClient client;

    public Charm() {
        super(Charm.MOD_ID, LOG);

        CharmMessages.init(this);
        CharmSounds.init(this);
        CharmCompat.init(this);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        super.onClientSetup(event);
        client = new CharmClient();
        MinecraftForge.EVENT_BUS.addListener(CharmClient::onClientTick);
    }
}