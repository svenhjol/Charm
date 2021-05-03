package svenhjol.charm.structure;

import svenhjol.charm.structure.dungeons.ForestDungeons;
import svenhjol.charm.structure.dungeons.MountainsDungeons;
import svenhjol.charm.structure.dungeons.PlainsDungeons;

public class DungeonBuilds {
    public static void init() {
        DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.Creeper());
        DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.Columns());
        DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.Pillar());
        DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.Shelf());

        DungeonGenerator.FOREST_DUNGEONS.add(new ForestDungeons.LittleGarden());
        DungeonGenerator.FOREST_DUNGEONS.add(new ForestDungeons.LittleTree());

        DungeonGenerator.MOUNTAINS_DUNGEONS.add(new MountainsDungeons.Pillar());
        DungeonGenerator.MOUNTAINS_DUNGEONS.add(new MountainsDungeons.Shelves());
        DungeonGenerator.MOUNTAINS_DUNGEONS.add(new MountainsDungeons.Wooden());
    }
}
