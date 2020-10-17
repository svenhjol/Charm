package svenhjol.charm.module.vanillachanges;

import svenhjol.charm.Charm;
import svenhjol.charm.client.MusicClient;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Playing a record in a jukebox stops background music from playing at the same.\n" +
    "Creative music tracks may also play in survival mode.")
public class MusicImprovements extends CharmModule {
    private MusicClient client;

    @Config(name = "Play Creative music", description = "If true, the six Creative music tracks may play in survival mode.")
    public static boolean playCreativeMusic = true;

    @Override
    public void clientInit() {
        client = new MusicClient(this);
    }
}
