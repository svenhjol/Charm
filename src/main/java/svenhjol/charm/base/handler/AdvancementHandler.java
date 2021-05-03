package svenhjol.charm.base.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import svenhjol.charm.module.Core;

import java.util.ArrayList;
import java.util.List;

public class AdvancementHandler {
    public static final List<Identifier> advancementsToRemove = new ArrayList<>();

    public static void init() {
        advancementsToRemove.clear();

        ModuleHandler.INSTANCE.getLoaders().forEach(loader -> loader.eachModule(m -> {
            if (!m.enabled || !Core.advancements)
                advancementsToRemove.addAll(m.advancements());
        }));
    }

    public static List<PlayerEntity> getPlayersInRange(World world, BlockPos pos) {
        return world.getNonSpectatingEntities(PlayerEntity.class, new Box(pos).expand(8.0D));
    }
}
