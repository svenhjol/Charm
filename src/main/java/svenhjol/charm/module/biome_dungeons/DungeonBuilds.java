package svenhjol.charm.module.biome_dungeons;

import svenhjol.charm.module.biome_dungeons.DungeonGenerator;
import svenhjol.charm.module.biome_dungeons.builds.*;

public class DungeonBuilds {
    public static void init() {
        svenhjol.charm.module.biome_dungeons.DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.Creeper());
        svenhjol.charm.module.biome_dungeons.DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.Columns());
        svenhjol.charm.module.biome_dungeons.DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.Pillar());
        svenhjol.charm.module.biome_dungeons.DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.Shelf());

        svenhjol.charm.module.biome_dungeons.DungeonGenerator.FOREST_DUNGEONS.add(new ForestDungeons.LittleGarden());
        svenhjol.charm.module.biome_dungeons.DungeonGenerator.FOREST_DUNGEONS.add(new ForestDungeons.LittleTree());
        svenhjol.charm.module.biome_dungeons.DungeonGenerator.FOREST_DUNGEONS.add(new ForestDungeons.Shelves());

        svenhjol.charm.module.biome_dungeons.DungeonGenerator.MOUNTAINS_DUNGEONS.add(new MountainsDungeons.Pillar());
        svenhjol.charm.module.biome_dungeons.DungeonGenerator.MOUNTAINS_DUNGEONS.add(new MountainsDungeons.Shelves());
        DungeonGenerator.MOUNTAINS_DUNGEONS.add(new MountainsDungeons.Wooden());
    }
}
