package svenhjol.charm.smithing;

import svenhjol.charm.smithing.feature.*;
import svenhjol.meson.Module;

public class CharmSmithing extends Module
{
    public CharmSmithing()
    {
        features.add(new DecreaseRepairCost());
        features.add(new ExtendTotemOfReturning());
        features.add(new ExtractBookXP());
        features.add(new ExtractEnchantments());
        features.add(new RecycleWithFlint());
        features.add(new TallowIncreasesDurability());
    }
}