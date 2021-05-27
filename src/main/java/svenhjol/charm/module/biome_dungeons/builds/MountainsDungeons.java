package svenhjol.charm.module.biome_dungeons.builds;

import svenhjol.charm.Charm;
import svenhjol.charm.world.CharmStructure;

public class MountainsDungeons {
    public static class Pillar extends CharmStructure {
        public Pillar() {
            super(Charm.MOD_ID, "dungeons", "mountains_pillar");
            addStart("mountains_pillar", 1);
        }
    }

    public static class Shelves extends CharmStructure {
        public Shelves() {
            super(Charm.MOD_ID, "dungeons", "mountains_shelves");
            addStart("mountains_shelves", 1);
        }
    }

    public static class Wooden extends CharmStructure {
        public Wooden() {
            super(Charm.MOD_ID, "dungeons", "mountains_wooden");
            addStart("mountains_wooden", 1);
        }
    }
}
