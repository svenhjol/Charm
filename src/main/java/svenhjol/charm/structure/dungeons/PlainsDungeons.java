package svenhjol.charm.structure.dungeons;

import svenhjol.charm.Charm;
import svenhjol.charm.base.structure.BaseStructure;

public class PlainsDungeons {
    public static class CobbleStairs extends BaseStructure {
        public CobbleStairs() {
            super(Charm.MOD_ID, "dungeons", "cobble_stairs");
            addStart("cobble_stairs_start1", 1);
        }
    }

    public static class CobbleTwolevel extends BaseStructure {
        public CobbleTwolevel() {
            super(Charm.MOD_ID, "dungeons", "cobble_twolevel");
            addStart("cobble_twolevel_start1", 1);
        }
    }

    public static class CobbleTwostep extends BaseStructure {
        public CobbleTwostep() {
            super(Charm.MOD_ID, "dungeons", "cobble_twostep");
            addStart("cobble_twostep_start1", 1);
        }
    }
}
