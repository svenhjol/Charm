package svenhjol.charm.handler;

import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.module.core.Core;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class AdvancementHandler {
    public static final List<ResourceLocation> modulesToRemove = new ArrayList<>();

    public static void init() {
        modulesToRemove.clear();

        ModuleHandler.INSTANCE.getLoaders().forEach(loader -> loader.eachModule(m -> {
            if (!m.enabled || !Core.advancements)
                modulesToRemove.add(m.getId());
        }));
    }

    public static List<Player> getPlayersInRange(Level world, BlockPos pos) {
        return world.getEntitiesOfClass(Player.class, new AABB(pos).inflate(8.0D));
    }
}
