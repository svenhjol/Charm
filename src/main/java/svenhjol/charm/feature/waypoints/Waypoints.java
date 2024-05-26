package svenhjol.charm.feature.waypoints;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.compasses_show_position.CompassesShowPosition;
import svenhjol.charm.feature.waypoints.common.Handlers;
import svenhjol.charm.feature.waypoints.common.Networking;
import svenhjol.charm.feature.waypoints.common.Registers;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Lodestones broadcast a message to a nearby player when a banner is placed on top.
    The title and color of the banner will be used in the message.""")
public final class Waypoints extends CommonFeature {
    public final Registers registers;
    public final Networking networking;
    public final Handlers handlers;

    @Configurable(
        name = "Broadcast distance",
        description = "Distance (in blocks) between a player and waypoint at which a message will be broadcast."
    )
    private static int broadcastDistance = 200;

    @Configurable(
        name = "Message duration",
        description = """
            Length of time (in seconds) that the broadcast message will remain on the screen.
            Set to zero to always be shown while the player is in range of a waypoint."""
    )
    private static int messageDuration = 20;

    @Configurable(
        name = "Show nearest waypoint on compass",
        description = """
            If true, the nearest waypoint will be shown when holding a compass.
            The closer to the waypoint source, the greater the number of stars shown next to the waypoint name.
            This setting is disabled if the CompassesShowPosition feature is not enabled."""
    )
    private static boolean showNearestWaypointOnCompass = true;

    public Waypoints(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        networking = new Networking(this);
        handlers = new Handlers(this);
    }

    public int broadcastDistance() {
        return Mth.clamp(broadcastDistance, 1, 2000);
    }

    public int messageDuration() {
        return Mth.clamp(messageDuration, 0, 60);
    }

    public boolean messageRemainsWhileInRange() {
        return messageDuration() == 0;
    }

    public boolean showNearestWaypointOnCompass() {
        return Resolve.feature(CompassesShowPosition.class).isEnabled() && showNearestWaypointOnCompass;
    }
}
