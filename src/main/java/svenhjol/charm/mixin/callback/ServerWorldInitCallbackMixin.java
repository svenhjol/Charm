package svenhjol.charm.mixin.callback;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.ServerWorldInitCallback;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerLevel.class)
public class ServerWorldInitCallbackMixin {
    /**
     * Fires the {@link ServerWorldInitCallback} event.
     *
     * This is used to load resources in Strange.
     */
    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hookInit(MinecraftServer server, Executor workerExecutor, LevelStorageSource.LevelStorageAccess session, ServerLevelData properties, ResourceKey<Level> worldKey, DimensionType dimensionType, ChunkProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long seed, List<CustomSpawner> spawners, boolean shouldTickTime, CallbackInfo ci) {
        ServerWorldInitCallback.EVENT.invoker().interact((ServerLevel)(Object)this);
    }
}
