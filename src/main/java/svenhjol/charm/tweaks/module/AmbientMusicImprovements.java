package svenhjol.charm.tweaks.module;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tweaks.client.AmbientMusicClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true,
    description = "Expands vanilla ambient music and prevents the background music when playing a music disc in a jukebox.")
public class AmbientMusicImprovements extends MesonModule {
    @Config(name = "Override delay", description = "Override the delay (in ticks) between ambient music tracks.  If zero, does not override the track default.")
    public static int maxDelayOverride = 3600;

    @Config(name = "Play Creative music", description = "If true, the six Creative music tracks may play in survival mode.")
    public static boolean playCreativeMusic = true;

    public static AmbientMusicClient client;

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        client = new AmbientMusicClient();
        client.setupClient(event);
        MinecraftForge.EVENT_BUS.register(client);
    }
}
