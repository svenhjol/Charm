package svenhjol.meson.helpers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class PlayerHelper
{
    /**
     * Basic way to teleport a player to co-ordinate in a dimension.
     * If the player is not in the specified dimension they will be transferred first.
     */
    public static void teleportPlayer(PlayerEntity player, BlockPos pos, int dim)
    {
        if (player.dimension.getId() != dim) {
            if (!player.world.isRemote && !player.isPassenger() && !player.isBeingRidden() && player.isNonBoss()) {
                DimensionType dimension = DimensionType.getById(dim);
                if (dimension != null) {
                    player.changeDimension(dimension);
                }
            }
        }

        player.setPositionAndUpdate(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

}
