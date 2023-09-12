package svenhjol.charm.mixin.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PatrolSpawner.class)
public interface PatrolSpawnerAccessor {
    @Accessor("nextTick")
    void setNextTick(int nextTick);

    @Invoker("spawnPatrolMember")
    boolean invokeSpawnPatrolMember(ServerLevel level, BlockPos pos, RandomSource random, boolean addLeader);
}
