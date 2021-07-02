package svenhjol.charm.helper;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

@SuppressWarnings({"unused"})
public class DimensionHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    public static boolean isOverworld(Level world) {
        return world.dimension() == Level.OVERWORLD;
    }

    public static boolean isNether(Level world) {
        return world.dimension() == Level.NETHER;
    }

    public static boolean isEnd(Level world) {
        return world.dimension() == Level.END;
    }

    public static boolean isDimension(Level world, ResourceLocation dimension) {
        return getDimension(world).equals(dimension);
    }

    public static boolean isDimension(Level world, ResourceKey<Level> key) {
        return world.dimension().equals(key);
    }

    public static ResourceLocation getDimension(Level world) {
        ResourceKey<Level> key = world.dimension();
        return key.location();
    }

    @Nullable
    public static ResourceKey<Level> getDimension(ResourceLocation dim) {
        if (Level.OVERWORLD.location().equals(dim)) {
            return Level.OVERWORLD;
        } else if (Level.NETHER.location().equals(dim)) {
            return Level.NETHER;
        } else if (Level.END.location().equals(dim)) {
            return Level.END;
        }

        return null;
    }

    public static void encodeDimension(ResourceKey<Level> worldKey, Consumer<Tag> consumer) {
        DataResult<Tag> result = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, worldKey);
        result.resultOrPartial(LOGGER::error).ifPresent(consumer);
    }

    public static Optional<ResourceKey<Level>> decodeDimension(Tag nbt) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt).result();
    }
}
