package svenhjol.charm.world;

import svenhjol.charm.world.feature.*;
import svenhjol.meson.Module;

public class CharmWorld extends Module
{
    public CharmWorld()
    {
        features.add(new BatBucket());
        features.add(new ChargedEmeralds());
        features.add(new IllusionersInRoofedForest());
        features.add(new MoreVillageBiomes());
        features.add(new NetherGoldDeposits());
        features.add(new Spectre());
        features.add(new SwampHutDecorations());
        features.add(new VillageDecorations());
        features.add(new VillagerTrades());
        features.add(new VindicatorsInRoofedForest());
    }
}