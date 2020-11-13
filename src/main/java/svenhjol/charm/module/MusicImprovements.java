package svenhjol.charm.module;

import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.client.MusicImprovementsClient;

@Module(mod = Charm.MOD_ID, client = MusicImprovementsClient.class, description = "Playing a record in a jukebox stops background music from playing at the same.\n" +
    "Creative music tracks may also play in survival mode.")
public class MusicImprovements extends CharmModule {
    @Config(name = "Play Creative music", description = "If true, the six Creative music tracks may play in survival mode.")
    public static boolean playCreativeMusic = true;
}
