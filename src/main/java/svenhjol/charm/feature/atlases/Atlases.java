package svenhjol.charm.feature.atlases;

import svenhjol.charm.feature.atlases.common.*;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Storage for maps that automatically updates the displayed map as you explore.")
public class Atlases extends CommonFeature {
    public final Advancements advancements;
    public final Registers registers;
    public final Handlers handlers;
    public final Networking networking;
    public final Providers providers;

    public static final int EMPTY_MAP_SLOTS = 3;

    @Configurable(name = "Open in off hand", description = "Allow opening the atlas while it is in the off-hand.")
    public static boolean offHandOpen = false;

    @Configurable(name = "Map scale", description = "Map scale used in atlases by default.")
    public static int defaultScale = 0;

    public Atlases(CommonLoader loader) {
        super(loader);

        advancements = new Advancements(this);
        handlers = new Handlers(this);
        networking = new Networking(this);
        registers = new Registers(this);
        providers = new Providers(this);
    }
}
