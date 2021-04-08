package svenhjol.charm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.Spawner;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.level.ServerWorldProperties;
import net.minecraft.world.level.storage.LevelStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.event.ServerWorldInitCallback;
import svenhjol.charm.module.SnowStorms;

import java.util.List;
import java.util.concurrent.Executor;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hookInit(MinecraftServer server, Executor workerExecutor, LevelStorage.Session session, ServerWorldProperties properties, RegistryKey<World> worldKey, DimensionType dimensionType, WorldGenerationProgressListener worldGenerationProgressListener, ChunkGenerator chunkGenerator, boolean debugWorld, long seed, List<Spawner> spawners, boolean shouldTickTime, CallbackInfo ci) {
        ServerWorldInitCallback.EVENT.invoker().interact((ServerWorld)(Object)this);
    }

    @Inject(
        method = "addEntity",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/world/ServerEntityManager;addEntity(Lnet/minecraft/world/entity/EntityLike;)Z"
        )
    )
    private void hookAddEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        AddEntityCallback.EVENT.invoker().interact(entity);
    }

    /**
     * Calls our AddEntityCallback event when a player
     * joins the game on the server side.
     *
     * Inspired by Forge's EntityJoinWorldEvent which
     * provides a hook at the same point in the code,
     * except allows it to be cancellable.
     */
    @Inject(
        method = "addPlayer",
        at = @At("HEAD")
    )
    private void hookAddPlayer(ServerPlayerEntity player, CallbackInfo ci) {
        AddEntityCallback.EVENT.invoker().interact(player);
    }

    @Inject(
        method = "tickChunk",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookTryPlaceSnow(WorldChunk chunk, int randomTickSpeed, CallbackInfo ci, ChunkPos chunkPos, boolean isRaining, int chunkX, int chunkZ) {
        SnowStorms.tryPlaceSnow((ServerWorld)(Object)this, chunkX, chunkZ);
    }
}
