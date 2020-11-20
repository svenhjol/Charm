package svenhjol.charm.base.helper;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

import javax.annotation.Nullable;

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

    public static boolean isDimension(World world, Identifier dimension) {
        return getDimension(world).equals(dimension);
    }

    public static boolean isDimension(World world, RegistryKey<World> key) {
        return world.getRegistryKey().equals(key);
    }

    public static Identifier getDimension(World world) {
        RegistryKey<World> key = world.getRegistryKey();
        return key.getValue();
    }

    @Nullable
    public static RegistryKey<World> getDimension(Identifier dim) {
        if (World.OVERWORLD.getValue().equals(dim)) {
            return World.OVERWORLD;
        } else if (World.NETHER.getValue().equals(dim)) {
            return World.NETHER;
        } else if (World.END.getValue().equals(dim)) {
            return World.END;
        }

        return null;
    }
}
