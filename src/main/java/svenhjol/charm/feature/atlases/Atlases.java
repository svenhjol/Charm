package svenhjol.charm.feature.atlases;

import svenhjol.charm.feature.atlases.common.Advancements;
import svenhjol.charm.feature.atlases.common.Handlers;
import svenhjol.charm.feature.atlases.common.Networking;
import svenhjol.charm.feature.atlases.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.common.CommonFeature;

public class Atlases extends CommonFeature {
    public static Advancements advancements;
    public static Registers registers;
    public static Handlers handlers;
    public static Networking networking;

    public static final int EMPTY_MAP_SLOTS = 3;

    @Configurable(name = "Open in off hand", description = "Allow opening the atlas while it is in the off-hand.")
    public static boolean offHandOpen = false;

    @Configurable(name = "Map scale", description = "Map scale used in atlases by default.")
    public static int defaultScale = 0;

    @Override
    public String description() {
        return "Storage for maps that automatically updates the displayed map as you explore.";
    }

    @Override
    public void setup() {
        advancements = new Advancements(this);
        handlers = new Handlers(this);
        networking = new Networking(this);
        registers = new Registers(this);
    }
}
