package svenhjol.charm.feature.arcane_purpur;

import svenhjol.charm.feature.arcane_purpur.common.Registers;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = """
    Arcane Purpur is a decorative block made from Purpur and Endermite Powder.
    If the ChorusTeleport feature is enabled, a Chiseled Arcane Purpur block
    allows teleportation when a chorus fruit is eaten within range of the block.""")
public final class ArcanePurpur extends CommonFeature {
    public final Registers registers;

    public ArcanePurpur(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }
}
