package svenhjol.charm.feature.compass_shows_position;

import svenhjol.charm.feature.compass_shows_position.client.Handlers;
import svenhjol.charm.feature.compass_shows_position.client.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(description = "Shows cardinal directions, XYZ coordinates and current biome when holding a compass.")
public class CompassShowsPosition extends ClientFeature {
    public final Registers registers;
    public final Handlers handlers;

    @Configurable(
        name = "Facing",
        description = "If true, shows the cardinal direction that the player is facing.",
        requireRestart = false
    )
    public static boolean showFacing = true;

    @Configurable(
        name = "Co-ordinates",
        description = "If true, shows the player's XYZ coordinates.",
        requireRestart = false
    )
    public static boolean showCoords = true;

    @Configurable(
        name = "Biome",
        description = "If true, shows the player's biome.",
        requireRestart = false
    )
    public static boolean showBiome = true;

    @Configurable(
        name = "Only show X and Z",
        description = "If true, only show the player's X and Z coordinates (not their height/depth).",
        requireRestart = false
    )
    public static boolean onlyXZ = false;

    @Configurable(
        name = "Show when sneaking",
        description = "If true, only show the compass overlay if the player is sneaking.",
        requireRestart = false
    )
    public static boolean onlyShowWhenSneaking = false;

    @Configurable(
        name = "Always show",
        description = "If true, the overlay will always be shown even if the player is not holding a compass.",
        requireRestart = false
    )
    public static boolean alwaysShow = false;

    @Configurable(
        name = "Compact view",
        description = "If true, the overlay will be shown in smaller text and in top left of the screen.",
        requireRestart = false
    )
    public static boolean compactView = false;

    public CompassShowsPosition(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }
}
