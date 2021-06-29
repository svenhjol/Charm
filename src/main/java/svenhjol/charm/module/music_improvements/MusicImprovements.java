package svenhjol.charm.module.music_improvements;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.module.CharmModule;

@Module(mod = Charm.MOD_ID, client = MusicImprovementsClient.class, description = "Playing a record in a jukebox stops background music from playing at the same.\n" +
    "Creative music tracks may also play in survival mode.",
    requiresMixins = {"PlaySoundCallback"})
public class MusicImprovements extends CharmModule {
    @Config(name = "Play Creative music", description = "If true, the six Creative music tracks may play in survival mode.")
    public static boolean playCreativeMusic = true;
}
