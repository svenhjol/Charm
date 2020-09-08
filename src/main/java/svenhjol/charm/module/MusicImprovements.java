package svenhjol.charm.module;

import svenhjol.charm.client.MusicClient;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(description = "Playing a record in a jukebox stops background music from playing at the same.\n" +
    "Creative music tracks may also play in survival mode.")
public class MusicImprovements extends MesonModule {
    private MusicClient client;

    @Config(name = "Play Creative music", description = "If true, the six Creative music tracks may play in survival mode.")
    public static boolean playCreativeMusic = true;

    @Override
    public void clientInit() {
        client = new MusicClient(this);
    }
}
