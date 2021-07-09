package svenhjol.charm.helper;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.0.0-charm
 */
public class DataBlockHelper {
    public static Direction getFacing(String direction) {
        if (direction.equals("east")) return Direction.EAST;
        if (direction.equals("south")) return Direction.SOUTH;
        if (direction.equals("west")) return Direction.WEST;
        return Direction.NORTH;
    }

    public static BlockState setFacing(BlockState state, DirectionProperty prop, String direction) {
        if (direction.equals("north")) state = state.setValue(prop, Direction.NORTH);
        if (direction.equals("east")) state = state.setValue(prop, Direction.EAST);
        if (direction.equals("south")) state = state.setValue(prop, Direction.SOUTH);
        if (direction.equals("west")) state = state.setValue(prop, Direction.WEST);
        return state;
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
