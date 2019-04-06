package svenhjol.meson;

import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler
{
    private static Map<String, BlockPos> structures = new HashMap<>();

    public static void setNearestStronghold(@Nullable BlockPos structure)
    {
        structures.put("Stronghold", structure);
    }

    public static void setNearestEndCity(@Nullable BlockPos structure)
    {
        structures.put("EndCity", structure);
    }

    public static void setNearestStructure(String name, @Nullable BlockPos structure)
    {
        structures.put(name, structure);
    }

    @Nullable
    public static BlockPos getNearestStronghold()
    {
        return structures.get("Stronghold");
    }

    @Nullable
    public static BlockPos getNearestEndCity()
    {
        return structures.get("EndCity");
    }
}
