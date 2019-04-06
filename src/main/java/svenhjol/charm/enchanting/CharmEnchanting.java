package svenhjol.charm.enchanting;

import svenhjol.charm.enchanting.feature.CurseBreak;
import svenhjol.charm.enchanting.feature.ExtraCurses;
import svenhjol.charm.enchanting.feature.Homing;
import svenhjol.charm.enchanting.feature.Salvage;
import svenhjol.meson.Module;

public class CharmEnchanting extends Module
{
    public CharmEnchanting()
    {
        features.add(new CurseBreak());
        features.add(new ExtraCurses());
        features.add(new Homing());
        features.add(new Salvage());
    }
}
