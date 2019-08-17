package svenhjol.charm.crafting;

import svenhjol.charm.crafting.feature.Crate;
import svenhjol.charm.crafting.feature.EnderPearlBlock;
import svenhjol.charm.crafting.feature.RottenFleshBlock;
import svenhjol.charm.crafting.feature.SmoothGlowstone;
import svenhjol.meson.Module;

public class CharmCrafting extends Module
{
    public CharmCrafting()
    {
        features.add(new Crate());
        features.add(new EnderPearlBlock());
        features.add(new RottenFleshBlock());
        features.add(new SmoothGlowstone());
    }
}
