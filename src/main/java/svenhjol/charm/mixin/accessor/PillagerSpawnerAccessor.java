package svenhjol.charm.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PatrolSpawner;

@Mixin(PatrolSpawner.class)
@CharmMixin(required = true)
public interface PillagerSpawnerAccessor {
    @Accessor
    void setTicksUntilNextSpawn(int ticks);

    @Invoker
    boolean invokeSpawnPillager(ServerLevel world, BlockPos pos, Random random, boolean captain);
}
