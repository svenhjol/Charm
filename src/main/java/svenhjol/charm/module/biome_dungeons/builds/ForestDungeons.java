package svenhjol.charm.module.biome_dungeons.builds;

import svenhjol.charm.Charm;
import svenhjol.charm.world.CharmStructure;

public class ForestDungeons {
    public static class LittleTree extends CharmStructure {
        public LittleTree() {
            super(Charm.MOD_ID, "dungeons", "forest_little_tree");
            addStart("forest_little_tree", 1);
        }
    }

    public static class LittleGarden extends CharmStructure {
        public LittleGarden() {
            super(Charm.MOD_ID, "dungeons", "forest_little_garden");
            addStart("forest_little_garden", 1);
        }
    }

    public static class Shelves extends CharmStructure {
        public Shelves() {
            super(Charm.MOD_ID, "dungeons", "forest_shelves");
            addStart("forest_shelves", 1);
        }
    }
}
