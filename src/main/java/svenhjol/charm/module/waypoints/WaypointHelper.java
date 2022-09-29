package svenhjol.charm.module.waypoints;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BannerBlockEntity;

import javax.annotation.Nullable;

public class WaypointHelper {
    @Nullable
    public static String getHash(Level level, BlockPos pos) {
        var banner = getLodestoneBanner(level, pos);
        if (banner == null) return null;

        var color = banner.getBaseColor();
        var title = banner.getDisplayName();

        if (title == null) return null;
        return level.dimension().location().toString() + pos.asLong() + color.getName() + title.getString();
    }

    @Nullable
    public static BannerBlockEntity getLodestoneBanner(Level level, BlockPos pos) {
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (block == Blocks.LODESTONE && level.getBlockEntity(pos.above()) instanceof BannerBlockEntity banner) {
            return banner;
        }

        return null;
    }

    public static boolean shouldPlaySound(Level level, BlockPos pos) {
        var state = level.getBlockState(pos);
        var block = state.getBlock();
        return block == Blocks.LODESTONE && level.getBlockState(pos.below()).getBlock() == Blocks.NOTE_BLOCK;
    }
}
