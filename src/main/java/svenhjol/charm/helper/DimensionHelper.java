package svenhjol.charm.helper;

import com.mojang.serialization.DataResult;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @version 1.0.2-charm
 */
@SuppressWarnings("unused")
public class DimensionHelper {
    private static final Logger LOGGER = LogManager.getLogger();

    public static boolean isOverworld(Level level) {
        return level.dimension() == Level.OVERWORLD;
    }

    public static boolean isNether(Level level) {
        return level.dimension() == Level.NETHER;
    }

    public static boolean isEnd(Level level) {
        return level.dimension() == Level.END;
    }

    public static boolean isDimension(Level level, ResourceLocation dimension) {
        return getDimension(level).equals(dimension);
    }

    public static boolean isDimension(Level level, ResourceKey<Level> key) {
        return level.dimension().equals(key);
    }

    public static ResourceLocation getDimension(Level world) {
        return getDimension(world.dimension());
    }

    public static ResourceLocation getDimension(ResourceKey<Level> key) {
        return key.location();
    }

    public static void encodeDimension(ResourceKey<Level> key, Consumer<Tag> consumer) {
        DataResult<Tag> result = Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, key);
        result.resultOrPartial(LOGGER::error).ifPresent(consumer);
    }

    public static Optional<ResourceKey<Level>> decodeDimension(Tag nbt) {
        return Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, nbt).result();
    }
}
