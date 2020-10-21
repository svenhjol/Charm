package svenhjol.charm.base.helper;

import net.minecraft.block.BlockState;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataBlockHelper {
    public static Direction getFacing(String direction) {
        if (direction.equals("east")) return Direction.EAST;
        if (direction.equals("south")) return Direction.SOUTH;
        if (direction.equals("west")) return Direction.WEST;
        return Direction.NORTH;
    }

    public static BlockState setFacing(BlockState state, DirectionProperty prop, String direction) {
        if (direction.equals("north")) state = state.with(prop, Direction.NORTH);
        if (direction.equals("east")) state = state.with(prop, Direction.EAST);
        if (direction.equals("south")) state = state.with(prop, Direction.SOUTH);
        if (direction.equals("west")) state = state.with(prop, Direction.WEST);
        return state;
    }

    public static Identifier getLootTable(String data, Identifier fallback) {
        Identifier lootTable = fallback;
        String loot = getValue("loot", data, "");

        if (!loot.isEmpty()) {
            List<Identifier> tables = LootHelper.getAllLootTables();

            for (Identifier res : tables) {
                String[] s = res.getPath().split("/");
                if (loot.contains(s[s.length - 1])) {
                    lootTable = res;
                    break;
                }
            }
        }

        return lootTable;
    }

    public static boolean getValue(String key, String name, boolean fallback) {
        String val = getValue(key, name, fallback ? "true" : "false");
        return val.isEmpty() ? fallback : Boolean.parseBoolean(val);
    }

    public static int getValue(String key, String name, int fallback) {
        int i = Integer.parseInt(getValue(key, name, "0"));
        return i == 0 ? fallback : i;
    }

    public static double getValue(String key, String name, double fallback) {
        double d = Double.parseDouble(getValue(key, name, "0"));
        return d == 0 ? fallback : d;
    }

    public static String getValue(String key, String data, String fallback) {
        String lookFor = key.endsWith("=") ? key : key + "=";

        if (data.contains(lookFor)) {
            Pattern p = Pattern.compile(lookFor + "([a-zA-Z0-9_:\\-]+)");
            Matcher m = p.matcher(data);
            if (m.find()) return m.group(1);
        }

        return fallback;
    }
}
