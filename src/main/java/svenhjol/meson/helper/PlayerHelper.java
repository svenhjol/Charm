package svenhjol.meson.helper;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

public class PlayerHelper
{
    /**
     * Tries to add item stack to player, drops if not possible.
     * @param player The player
     * @param stack The stack to add/drop
     * @return True if able to add to player inv, false if dropped
     */
    public static boolean addOrDropStack(PlayerEntity player, ItemStack stack)
    {
        if (!player.inventory.addItemStackToInventory(stack)) {
            player.dropItem(stack, false);
            return false;
        }
        return true;
    }

    public static void setHeldItem(PlayerEntity player, Hand hand, ItemStack item)
    {
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getCount() == 1) {
            player.setHeldItem(hand, item);
        } else {
            stack.shrink(1);
            if (stack.getCount() == 0) {
                player.setHeldItem(hand, item);
            } else {
                addOrDropStack(player, item);
            }
        }
    }

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
