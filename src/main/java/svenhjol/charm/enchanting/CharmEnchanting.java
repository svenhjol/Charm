package svenhjol.charm.enchanting;

import svenhjol.charm.enchanting.feature.*;
import svenhjol.meson.Module;

public class CharmEnchanting extends Module
{
    public CharmEnchanting()
    {
        features.add(new CurseBreak());
        features.add(new ExtraCurses());
        features.add(new Homing());
        features.add(new Magnetic());
        features.add(new Salvage());
    }
}
