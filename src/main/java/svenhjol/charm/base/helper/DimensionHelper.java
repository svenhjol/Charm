package svenhjol.charm.base.helper;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class DimensionHelper {
    public static boolean isDimension(World world, Identifier dim) {
        return getDimension(world).equals(dim);
    }

    public static Identifier getDimension(World world) {
        RegistryKey<World> key = world.getRegistryKey();
        return key.getValue();
    }
}
