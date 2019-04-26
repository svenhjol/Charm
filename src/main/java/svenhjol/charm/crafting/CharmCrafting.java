package svenhjol.charm.crafting;

import svenhjol.charm.crafting.feature.*;
import svenhjol.meson.Module;

public class CharmCrafting extends Module
{
    public CharmCrafting()
    {
        features.add(new Barrel());
        features.add(new BookshelfChest());
        features.add(new Composter());
        features.add(new Crate());
        features.add(new GunpowderBlock());
        features.add(new Lantern());
        features.add(new RedstoneSand());
        features.add(new RottenFleshBlock());
        features.add(new SuspiciousSoup());
    }
}