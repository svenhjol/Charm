package svenhjol.charm.module.music_improvements;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmCommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Playing a record in a jukebox stops background music from playing at the same.\n" +
    "Creative music tracks may also play in survival mode.")
public class MusicImprovements extends CharmCommonModule {
    @Config(name = "Play Creative music", description = "If true, the six Creative music tracks may play in survival mode.")
    public static boolean playCreativeMusic = true;
}
