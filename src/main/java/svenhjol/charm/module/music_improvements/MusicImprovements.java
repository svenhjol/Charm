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

    @Config(name = "Show active channels in debug mode", description = "If true and debug mode is enabled, show the number of active channels in the log. Only useful for debugging the sound client.")
    public static boolean debugActiveChannels = false;
}
