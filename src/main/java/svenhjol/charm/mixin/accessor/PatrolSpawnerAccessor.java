package svenhjol.charm.mixin.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

import java.util.Random;

@Mixin(PatrolSpawner.class)
@CharmMixin(required = true)
public interface PatrolSpawnerAccessor {
    @Accessor("nextTick")
    void setTicksUntilNextSpawn(int ticks);

    @Invoker
    boolean invokeSpawnPatrolMember(ServerLevel world, BlockPos pos, Random random, boolean captain);
}
