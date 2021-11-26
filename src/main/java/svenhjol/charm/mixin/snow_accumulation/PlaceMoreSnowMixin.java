package svenhjol.charm.mixin.snow_accumulation;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.module.snow_accumulation.SnowAccumulation;

@Mixin(ServerLevel.class)
public class PlaceMoreSnowMixin {
    /**
     * Call {@link SnowAccumulation#tryPlaceSnow} each tick to add more snow layers when snowing.
     */
    @Inject(
        method = "tickChunk",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiling/ProfilerFiller;popPush(Ljava/lang/String;)V",
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookTryPlaceSnow(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci, ChunkPos chunkPos, boolean isRaining, int chunkX, int chunkZ) {
        SnowAccumulation.tryPlaceSnow((ServerLevel)(Object)this, chunkX, chunkZ);
    }
}
