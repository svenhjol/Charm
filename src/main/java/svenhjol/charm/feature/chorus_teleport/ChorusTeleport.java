package svenhjol.charm.feature.chorus_teleport;

import svenhjol.charm.feature.chorus_teleport.common.Advancements;
import svenhjol.charm.feature.chorus_teleport.common.Handlers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Eating a chorus fruit within range of a chiseled arcane purpur block teleports the player to the block.")
public class ChorusTeleport extends CommonFeature {
    public final Advancements advancements;
    public final Handlers handlers;

    @Configurable(
        name = "Range",
        description = "Range (in blocks) in which a player will be teleported.",
        requireRestart = false
    )
    public static int range = 8;

    public ChorusTeleport(CommonLoader loader) {
        super(loader);

        advancements = new Advancements(this);
        handlers = new Handlers(this);
    }
}
