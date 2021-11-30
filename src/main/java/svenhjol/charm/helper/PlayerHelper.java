package svenhjol.charm.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.*;

/**
 * @version 1.0.0-charm
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public class PlayerHelper {
    /**
     * Tries to add item stack to player, drops if not possible.
     */
    public static boolean addOrDropStack(Player player, ItemStack stack) {
        if (!player.inventory.add(stack)) {
            player.drop(stack, true);
            return false;
        }
        return true;
    }

    public static void teleport(Level level, BlockPos pos, Player player) {
        if (!level.isClientSide) {
            ServerLevel serverLevel = (ServerLevel) level;

            double x = pos.getX() + 0.5D;
            double y = pos.getY() + 0.25D;
            double z = pos.getZ() + 0.5D;
            float yaw = player.getYRot();
            float pitch = player.getXRot();
            Set<ClientboundPlayerPositionPacket.RelativeArgument> flags = EnumSet.noneOf(ClientboundPlayerPositionPacket.RelativeArgument.class);

            ChunkPos chunkPos = new ChunkPos(new BlockPos(x, y, z));
            serverLevel.getChunkSource().addRegionTicket(TicketType.POST_TELEPORT, chunkPos, 1, player.getId());
            player.stopRiding();

            if (player.isSleeping())
                player.stopSleepInBed(true, true);

            if (level == player.level) {
                ((ServerPlayer)player).connection.teleport(x, y, z, yaw, pitch, flags);
            } else {
                ((ServerPlayer)player).teleportTo(serverLevel, x, y, z, yaw, pitch);
            }
        }
    }

    public static List<Player> getPlayersInRange(Level level, BlockPos pos) {
        return level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8.0D));
    }

    public static Optional<String> getPlayerName(MinecraftServer server, UUID player) {
        Optional<ServerPlayer> learnedPlayer = Optional.ofNullable(server.getPlayerList().getPlayer(player));
        return learnedPlayer.map(serverPlayer -> serverPlayer.getName().getString());
    }
}
