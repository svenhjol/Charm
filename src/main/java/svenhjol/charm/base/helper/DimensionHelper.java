package svenhjol.charm.base.helper;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class DimensionHelper {
    private static final Logger LOGGER = LogManager.getLogger();

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

    public static void encodeDimension(RegistryKey<World> worldKey, Consumer<NbtElement> consumer) {
        DataResult<NbtElement> result = World.CODEC.encodeStart(NbtOps.INSTANCE, worldKey);
        result.resultOrPartial(LOGGER::error).ifPresent(consumer);
    }

    public static Optional<RegistryKey<World>> decodeDimension(NbtElement nbt) {
        return World.CODEC.parse(NbtOps.INSTANCE, nbt).result();
    }
}
