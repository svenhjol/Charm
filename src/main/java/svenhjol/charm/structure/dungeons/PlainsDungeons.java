package svenhjol.charm.structure.dungeons;

import svenhjol.charm.Charm;
import svenhjol.charm.base.structure.BaseStructure;

public class PlainsDungeons {
    public static class Columns extends BaseStructure {
        public Columns() {
            super(Charm.MOD_ID, "dungeons", "plains_columns");
            addStart("plains_columns", 1);
        }
    }

    public static class Pillar extends BaseStructure {
        public Pillar() {
            super(Charm.MOD_ID, "dungeons", "plains_pillar");
            addStart("plains_pillar", 1);
        }
    }

    public static class Shelf extends BaseStructure {
        public Shelf() {
            super(Charm.MOD_ID, "dungeons", "plains_shelf");
            addStart("plains_shelf", 1);
        }
    }
}
