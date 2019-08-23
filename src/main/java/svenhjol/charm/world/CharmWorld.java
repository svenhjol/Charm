package svenhjol.charm.world;

import svenhjol.charm.world.feature.*;
import svenhjol.meson.Module;

public class CharmWorld extends Module
{
    public CharmWorld()
    {
        features.add(new AbandonedCrates());
        features.add(new BatBucket());
        features.add(new ChargedEmeralds());
        features.add(new EndermitePowder());
        features.add(new EndPortalRunes());
        features.add(new IllusionersInRoofedForest());
        features.add(new Moonstone());
        features.add(new MoreVillageBiomes());
        features.add(new NetherGoldDeposits());
        features.add(new Spectre());
        features.add(new SpectreHaunting());
        features.add(new StructureMaps());
        features.add(new SwampHutDecorations());
        features.add(new TotemOfReturning());
        features.add(new TotemOfShielding());
        features.add(new VillageDecorations());
        features.add(new VillagerTrades());
        features.add(new VindicatorsInRoofedForest());
    }
}