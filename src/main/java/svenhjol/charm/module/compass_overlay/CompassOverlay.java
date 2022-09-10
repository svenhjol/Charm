package svenhjol.charm.module.compass_overlay;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Shows cardinal points and XYZ coordinates when holding a compass.")
public class CompassOverlay extends CharmModule {
    @Config(name = "Facing", description = "If true, shows the cardinal direction that the player is facing.")
    public static boolean showFacing = true;

    @Config(name = "Co-ordinates", description = "If true, shows the player's XYZ coordinates.")
    public static boolean showCoords = true;

    @Config(name = "Biome", description = "If true, shows the player's biome.")
    public static boolean showBiome = true;

    @Config(name = "Only show X and Z", description = "If true, only show the player's X and Z coordinates (not their height/depth).")
    public static boolean onlyXZ = false;

    @Config(name = "Show when sneaking", description = "If true, only show the compass overlay if the player is sneaking.")
    public static boolean onlyShowWhenSneaking = false;
}
