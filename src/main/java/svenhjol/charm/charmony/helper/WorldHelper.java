package svenhjol.charm.charmony.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.List;

@SuppressWarnings("unused")
public final class WorldHelper {
    public static boolean isDay(Player player) {
        var level = player.level();
        var dayTime = level.getDayTime() % 24000;
        return dayTime >= 0 && dayTime < 12700;
    }

    public static boolean isNight(Player player) {
        var level = player.level();
        var dayTime = level.getDayTime() % 24000;
        return dayTime >= 12700;
    }

    public static boolean isThundering(Player player) {
        var level = player.level();
        return level.isThundering();
    }

    public static boolean isOutside(Player player) {
        var level = player.level();
        if (player.isUnderWater()) return false;

        var blocks = 24;
        var start = 1;

        var playerPos = player.blockPosition();

        if (level.canSeeSky(playerPos)) return true;
        if (level.canSeeSkyFromBelowWater(playerPos)) return true;

        for (int i = start; i < start + blocks; i++) {
            var check = new BlockPos(playerPos.getX(), playerPos.getY() + i, playerPos.getZ());
            var state = level.getBlockState(check);
            var block = state.getBlock();

            if (level.isEmptyBlock(check)) continue;

            // TODO: Tag for glass here
            if (state.is(Blocks.GLASS)
                || (block instanceof RotatedPillarBlock && state.is(BlockTags.LOGS))
                || block instanceof LeavesBlock
                || block instanceof HugeMushroomBlock
                || block instanceof StemBlock
            ) continue;

            if (level.canSeeSky(check)) return true;
            if (level.canSeeSkyFromBelowWater(check)) return true;
            if (state.canOcclude()) return false;
        }

        return level.canSeeSky(playerPos.above(blocks));
    }

    public static float distanceFromGround(Player player, int check) {
        var level = player.level();
        var pos = player.blockPosition();
        var playerHeight = pos.getY();

        // Sample points.
        var samples = List.of(
            pos.east(check),
            pos.west(check),
            pos.north(check),
            pos.south(check)
        );

        var avg = 0;
        for (BlockPos sample : samples) {
            avg += level.getHeight(Heightmap.Types.WORLD_SURFACE, sample.getX(), sample.getZ());
        }
        avg /= samples.size();
        return Math.max(0.0F, playerHeight - avg);
    }

    public static boolean isBelowSeaLevel(Player player) {
        var level = player.level();
        return player.blockPosition().getY() < level.getSeaLevel();
    }

    public static double getDistanceSquared(BlockPos pos1, BlockPos pos2) {
        double d0 = pos1.getX();
        double d1 = pos1.getZ();
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }
}
