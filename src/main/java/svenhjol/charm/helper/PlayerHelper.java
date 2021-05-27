package svenhjol.charm.helper;

import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldSaveHandler;
import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;
import svenhjol.charm.mixin.accessor.WorldSaveHandlerAccessor;

import java.io.File;
import java.util.EnumSet;
import java.util.Set;

public class PlayerHelper {
    /**
     * Tries to add item stack to player, drops if not possible.
     *
     * @param player The player
     * @param stack  The stack to add/drop
     * @return True if able to add to player inv, false if dropped
     */
    public static boolean addOrDropStack(PlayerEntity player, ItemStack stack) {
        if (!getInventory(player).insertStack(stack)) {
            player.dropItem(stack, true);
            return false;
        }
        return true;
    }

    public static PlayerInventory getInventory(PlayerEntity player) {
        return ((PlayerEntityAccessor)player).getInventory();
    }

    public static PlayerAbilities getAbilities(PlayerEntity player) {
        return ((PlayerEntityAccessor)player).getAbilities();
    }

    public static void teleport(World world, BlockPos pos, PlayerEntity player) {
        if (!world.isClient) {
            ServerWorld serverWorld = (ServerWorld) world;

            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.25D;
            double z = pos.getZ() + 0.5D;
            float yaw = player.getYaw();
            float pitch = player.getPitch();
            Set<PlayerPositionLookS2CPacket.Flag> flags = EnumSet.noneOf(PlayerPositionLookS2CPacket.Flag.class);

            ChunkPos chunkPos = new ChunkPos(new BlockPos(x, y, z));
            serverWorld.getChunkManager().addTicket(ChunkTicketType.POST_TELEPORT, chunkPos, 1, player.getId());
            player.stopRiding();

            if (player.isSleeping())
                player.wakeUp(true, true);

            if (world == player.world) {
                ((ServerPlayerEntity)player).networkHandler.requestTeleport(x, y, z, yaw, pitch, flags);
            } else {
                ((ServerPlayerEntity)player).teleport(serverWorld, x, y, z, yaw, pitch);
            }
        }
    }

    public static File getPlayerDataDir(WorldSaveHandler saveHandler) {
        return ((WorldSaveHandlerAccessor) saveHandler).getPlayerDataDir();
    }
}
