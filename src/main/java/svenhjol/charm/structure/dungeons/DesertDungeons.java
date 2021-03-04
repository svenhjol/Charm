package svenhjol.charm.structure.dungeons;

import svenhjol.charm.Charm;
import svenhjol.charm.base.structure.BaseStructure;

import java.util.HashMap;
import java.util.Map;

public class DesertDungeons {
    public static class SandTrap extends BaseStructure {
        public SandTrap() {
            super(Charm.MOD_ID, "dungeons", "sand_trap");

            addStart("sand_trap_start1", 1);

            Map<String, Integer> ROOMS = new HashMap<>();
            ROOMS.put("sand_trap_room1", 1);
            ROOMS.put("sand_trap_room2", 1);
            registerPool("rooms", ROOMS);
        }
    }
}
