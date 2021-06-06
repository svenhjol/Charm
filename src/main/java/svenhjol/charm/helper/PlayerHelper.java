package svenhjol.charm.helper;

import svenhjol.charm.mixin.accessor.PlayerEntityAccessor;
import svenhjol.charm.mixin.accessor.WorldSaveHandlerAccessor;

import java.io.File;
import java.util.EnumSet;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.PlayerDataStorage;

public class PlayerHelper {
    /**
     * Tries to add item stack to player, drops if not possible.
     *
     * @param player The player
     * @param stack  The stack to add/drop
     * @return True if able to add to player inv, false if dropped
     */
    public static boolean addOrDropStack(Player player, ItemStack stack) {
        if (!getInventory(player).add(stack)) {
            player.drop(stack, true);
            return false;
        }
        return true;
    }

    public static Inventory getInventory(Player player) {
        return ((PlayerEntityAccessor)player).getInventory();
    }

    public static Abilities getAbilities(Player player) {
        return ((PlayerEntityAccessor)player).getAbilities();
    }

    public static void teleport(Level world, BlockPos pos, Player player) {
        if (!world.isClientSide) {
            ServerLevel serverWorld = (ServerLevel) world;

            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.25D;
            double z = pos.getZ() + 0.5D;
            float yaw = player.getYRot();
            float pitch = player.getXRot();
            Set<ClientboundPlayerPositionPacket.RelativeArgument> flags = EnumSet.noneOf(ClientboundPlayerPositionPacket.RelativeArgument.class);

            ChunkPos chunkPos = new ChunkPos(new BlockPos(x, y, z));
            serverWorld.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkPos, 1, player.getId());
            player.stopRiding();

            if (player.isSleeping())
                player.stopSleepInBed(true, true);

            if (world == player.level) {
                ((ServerPlayer)player).connection.teleport(x, y, z, yaw, pitch, flags);
            } else {
                ((ServerPlayer)player).teleportTo(serverWorld, x, y, z, yaw, pitch);
            }
        }
    }

    public static File getPlayerDataDir(PlayerDataStorage saveHandler) {
        return ((WorldSaveHandlerAccessor) saveHandler).getPlayerDataDir();
    }
}
