package svenhjol.charm.structure.dungeons;

import svenhjol.charm.Charm;
import svenhjol.charm.base.structure.BaseStructure;

public class MountainsDungeons {
    public static class Pillar extends BaseStructure {
        public Pillar() {
            super(Charm.MOD_ID, "dungeons", "mountains_pillar");
            addStart("mountains_pillar", 1);
        }
    }

    public static class Shelves extends BaseStructure {
        public Shelves() {
            super(Charm.MOD_ID, "dungeons", "mountains_shelves");
            addStart("mountains_shelves", 1);
        }
    }

    public static class Wooden extends BaseStructure {
        public Wooden() {
            super(Charm.MOD_ID, "dungeons", "mountains_wooden");
            addStart("mountains_wooden", 1);
        }
    }
}
