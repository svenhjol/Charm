package svenhjol.charm.smithing;

import svenhjol.charm.smithing.feature.*;
import svenhjol.charm.tweaks.feature.FurnacesRecycleMore;
import svenhjol.charm.tweaks.feature.RestrictFurnaceInput;
import svenhjol.meson.Module;

public class CharmSmithing extends Module
{
    public CharmSmithing()
    {
        features.add(new DecreaseRepairCost());
        features.add(new ExtendTotemOfReturning());
        features.add(new ExtractBookXP());
        features.add(new ExtractEnchantments());
        features.add(new TallowIncreasesDurability());
    }
}