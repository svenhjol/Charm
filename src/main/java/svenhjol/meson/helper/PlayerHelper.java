package svenhjol.meson.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;

import java.util.function.Consumer;

@SuppressWarnings("unused")
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

    public static void damageHeldItem(PlayerEntity player, Hand hand, ItemStack stack, int damage)
    {
        stack.damageItem(damage, player, (p) -> { player.sendBreakAnimation(hand); });
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
    public static void teleport(PlayerEntity player, BlockPos pos, int dim)
    {
        teleport(player, pos, dim, p -> {});
    }

    public static void teleport(PlayerEntity player, BlockPos pos, int dim, Consumer<BlockPos> onTeleport)
    {
        World world = player.world;
        if (world.isRemote) return;

        changeDimension(player, dim);

        ((ServerPlayerEntity)player).teleport((ServerWorld)world, pos.getX(), pos.getY(), pos.getZ(), player.rotationYaw, player.rotationPitch);
        BlockPos updateDest = world.getHeight(Heightmap.Type.MOTION_BLOCKING, pos);
        player.setPositionAndUpdate(updateDest.getX(), updateDest.getY() + 1, updateDest.getZ()); // TODO check landing block

        onTeleport.accept(player.getPosition());
    }

    public static void teleportSurface(PlayerEntity player, BlockPos pos, int dim)
    {
        teleportSurface(player, pos, dim, p -> {});
    }

    public static void teleportSurface(PlayerEntity player, BlockPos pos, int dim, Consumer<BlockPos> onTeleport)
    {
        World world = player.world;
        if (world.isRemote) return;

        // TODO check this actually does anything - it's supposed to preload the chunk
        ChunkPos chunkpos = new ChunkPos(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
        ((ServerWorld)world).getChunkProvider().func_217228_a(TicketType.POST_TELEPORT, chunkpos, 1, player.getEntityId());

        Minecraft.getInstance().deferTask(() -> {
            teleport(player, pos, dim, (p) -> {
                for (int y = world.getHeight(); y > 0; y--) {
                    BlockPos pp = new BlockPos(p.getX(), y, p.getZ());
                    if (world.getBlockState(pp).isAir()
                        && !world.getBlockState(pp.down()).isAir()
                    ) {
                        player.setPositionAndUpdate(pp.getX(), y, pp.getZ());
                        onTeleport.accept(player.getPosition());
                        break;
                    }
                }
            });
        });
    }

    public static void changeDimension(PlayerEntity player, int dim)
    {
        if (player.world.isRemote || player.dimension.getId() == dim) return;

        if (!player.isPassenger() && !player.isBeingRidden() && player.isNonBoss()) {
            DimensionType dimension = DimensionType.getById(dim);
            if (dimension != null) {
                player.changeDimension(dimension);
            }
        }
    }
}
