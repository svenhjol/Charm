package svenhjol.charm.mixin.accessor;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.PillagerSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Random;

@Mixin(PillagerSpawner.class)
public interface PillagerSpawnerAccessor {
    @Accessor
    void setTicksUntilNextSpawn(int ticks);

    @Invoker
    boolean invokeSpawnPillager(ServerWorld world, BlockPos pos, Random random, boolean captain);
}
