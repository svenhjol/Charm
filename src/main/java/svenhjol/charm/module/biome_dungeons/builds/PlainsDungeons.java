package svenhjol.charm.module.biome_dungeons.builds;

import svenhjol.charm.Charm;
import svenhjol.charm.world.CharmStructure;

public class PlainsDungeons {
    public static class Creeper extends CharmStructure {
        public Creeper() {
            super(Charm.MOD_ID, "dungeons", "plains_creeper");
            addStart("plains_creeper", 1);
        }
    }

    public static class Columns extends CharmStructure {
        public Columns() {
            super(Charm.MOD_ID, "dungeons", "plains_columns");
            addStart("plains_columns", 1);
        }
    }

    public static class Pillar extends CharmStructure {
        public Pillar() {
            super(Charm.MOD_ID, "dungeons", "plains_pillar");
            addStart("plains_pillar", 1);
        }
    }

    public static class Shelf extends CharmStructure {
        public Shelf() {
            super(Charm.MOD_ID, "dungeons", "plains_shelf");
            addStart("plains_shelf", 1);
        }
    }
}
