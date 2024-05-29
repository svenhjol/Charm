package svenhjol.charm.charmony.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;

@SuppressWarnings("unused")
public final class PlayerHelper {
    public static List<Player> getPlayersInRange(Level level, BlockPos pos, double range) {
        return level.getEntitiesOfClass(Player.class, new AABB(pos).inflate(range));
    }
}
