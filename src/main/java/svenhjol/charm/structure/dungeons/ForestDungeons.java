package svenhjol.charm.structure.dungeons;

import svenhjol.charm.Charm;
import svenhjol.charm.base.structure.BaseStructure;

public class ForestDungeons {
    public static class LittleTree extends BaseStructure {
        public LittleTree() {
            super(Charm.MOD_ID, "dungeons", "forest_little_tree");
            addStart("forest_little_tree", 1);
        }
    }

    public static class LittleGarden extends BaseStructure {
        public LittleGarden() {
            super(Charm.MOD_ID, "dungeons", "forest_little_garden");
            addStart("forest_little_garden", 1);
        }
    }
}
