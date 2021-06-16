package svenhjol.charm.handler;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class AdvancementHandler {
    public static final List<ResourceLocation> modulesToRemove = new ArrayList<>();

    public static List<Player> getPlayersInRange(Level world, BlockPos pos) {
        return world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8.0D));
    }
}
