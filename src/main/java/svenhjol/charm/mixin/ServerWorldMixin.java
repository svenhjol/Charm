package svenhjol.charm.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.module.SnowStorms;

@Mixin(ServerWorld.class)
public class ServerWorldMixin {
    @Inject(
        method = "addEntity",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/server/world/ServerWorld.getChunk(IILnet/minecraft/world/chunk/ChunkStatus;Z)Lnet/minecraft/world/chunk/Chunk;"
        )
    )
    private void hookAddEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        AddEntityCallback.EVENT.invoker().interact(entity);
    }

    @Inject(
        method = "loadEntity",
        at = @At(
            value = "INVOKE",
            target = "net/minecraft/server/world/ServerWorld.loadEntityUnchecked(Lnet/minecraft/entity/Entity;)V"
        )
    )
    private void hookLoadEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
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
