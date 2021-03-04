package svenhjol.charm.structure;

import svenhjol.charm.structure.dungeons.DesertDungeons;
import svenhjol.charm.structure.dungeons.PlainsDungeons;

public class DungeonBuilds {
    public static void init() {
        DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.CobbleStairs());
        DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.CobbleTwostep());
        DungeonGenerator.PLAINS_DUNGEONS.add(new PlainsDungeons.CobbleTwolevel());
        DungeonGenerator.DESERT_DUNGEONS.add(new DesertDungeons.SandTrap());
    }
}
