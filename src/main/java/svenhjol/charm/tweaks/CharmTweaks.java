package svenhjol.charm.tweaks;

import svenhjol.charm.tweaks.feature.*;
import svenhjol.meson.Module;

public class CharmTweaks extends Module
{
    public CharmTweaks()
    {
        features.add(new ExtraRecords());
        features.add(new PickaxesBreakPistons());
        features.add(new RemovePotionGlint());
        features.add(new StackableEnchantedBooks());
        features.add(new StackablePotions());
        features.add(new UseTotemFromInventory());
    }
}
