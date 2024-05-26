package svenhjol.charm.feature.compasses_show_position;

import svenhjol.charm.feature.compasses_show_position.client.Handlers;
import svenhjol.charm.feature.compasses_show_position.client.Providers;
import svenhjol.charm.feature.compasses_show_position.client.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;

@Feature(description = "Shows cardinal directions, XYZ coordinates and current biome when holding a compass.")
public class CompassesShowPosition extends ClientFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Providers providers;

    @Configurable(
        name = "Facing",
        description = "If true, shows the cardinal direction that the player is facing.",
        requireRestart = false
    )
    private static boolean showFacing = true;

    @Configurable(
        name = "Co-ordinates",
        description = "If true, shows the player's XYZ coordinates.",
        requireRestart = false
    )
    private static boolean showCoords = true;

    @Configurable(
        name = "Biome",
        description = "If true, shows the player's biome.",
        requireRestart = false
    )
    private static boolean showBiome = true;

    @Configurable(
        name = "Only show X and Z",
        description = "If true, only show the player's X and Z coordinates (not their height/depth).",
        requireRestart = false
    )
    private static boolean onlyShowXZ = false;

    @Configurable(
        name = "Show when sneaking",
        description = "If true, only show the compass overlay if the player is sneaking.",
        requireRestart = false
    )
    private static boolean onlyShowWhenSneaking = false;

    @Configurable(
        name = "Always show",
        description = "If true, the overlay will always be shown even if the player is not holding a compass.",
        requireRestart = false
    )
    private static boolean alwaysShow = false;

    @Configurable(
        name = "Compact view",
        description = "If true, the overlay will be shown in smaller text and in top left of the screen.",
        requireRestart = false
    )
    private static boolean compactView = false;

    public CompassesShowPosition(ClientLoader loader) {
        super(loader);

        registers = new Registers(this);
        handlers = new Handlers(this);
        providers = new Providers(this);
    }

    @Override
    public boolean canBeDisabled() {
        return true;
    }

    public boolean showFacing() {
        return showFacing;
    }

    public boolean showCoords() {
        return showCoords;
    }

    public boolean showBiome() {
        return showBiome;
    }

    public boolean onlyShowXZ() {
        return onlyShowXZ;
    }

    public boolean onlyShowWhenSneaking() {
        return onlyShowWhenSneaking;
    }

    public boolean alwaysShow() {
        return alwaysShow;
    }

    public boolean compactView() {
        return compactView;
    }

}
