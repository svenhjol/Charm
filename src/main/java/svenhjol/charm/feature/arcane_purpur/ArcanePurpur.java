package svenhjol.charm.feature.arcane_purpur;

import net.minecraft.util.Mth;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;
import svenhjol.charm.feature.arcane_purpur.common.Advancements;
import svenhjol.charm.feature.arcane_purpur.common.Handlers;
import svenhjol.charm.feature.arcane_purpur.common.Registers;

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
    private static int teleportRange = 12;

    public ArcanePurpur(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        advancements = new Advancements(this);
        handlers = new Handlers(this);
    }

    public int teleportRange() {
        return Mth.clamp(teleportRange, 0, 64);
    }
}
