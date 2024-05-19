package svenhjol.charm.feature.arcane_purpur;

import svenhjol.charm.feature.arcane_purpur.common.Advancements;
import svenhjol.charm.feature.arcane_purpur.common.Handlers;
import svenhjol.charm.feature.arcane_purpur.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Arcane Purpur is a decorative block made from Purpur and Endermite Powder.
    Chiseled Arcane Purpur allows teleportation when a chorus fruit is eaten within range of the block.""")
public final class ArcanePurpur extends CommonFeature {
    public final Registers registers;
    public final Advancements advancements;
    public final Handlers handlers;

    @Configurable(
        name = "Teleport range",
        description = "Range (in blocks) in which a player will be teleported to a chiseled arcane purpur block when eating a chorus fruit.",
        requireRestart = false
    )
    public static int range = 12;

    public ArcanePurpur(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
        handlers = new Handlers(this);
    }
}
