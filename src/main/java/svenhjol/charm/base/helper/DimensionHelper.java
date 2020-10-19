package svenhjol.charm.base.helper;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class DimensionHelper {
    public static boolean isOverworld(World world) {
        return world.getRegistryKey() == World.OVERWORLD;
    }

    public static boolean isNether(World world) {
        return world.getRegistryKey() == World.NETHER;
    }

    public static boolean isEnd(World world) {
        return world.getRegistryKey() == World.END;
    }

    public static boolean isDimension(World world, Identifier dim) {
        return getDimension(world).equals(dim);
    }

    public static Identifier getDimension(World world) {
        RegistryKey<World> key = world.getRegistryKey();
        return key.getValue();
    }
}
