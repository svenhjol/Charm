package svenhjol.charm.mixin.snow_storms;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.module.snow_storms.SnowStorms;

@Mixin(ServerLevel.class)
public class PlaceMoreSnowMixin {

    /**
     * Call {@link SnowStorms#tryPlaceSnow} each tick to add
     * more snow during a thunderstorm.
     */
    @Inject(
        method = "tickChunk",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V",
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookTryPlaceSnow(LevelChunk chunk, int randomTickSpeed, CallbackInfo ci, ChunkPos chunkPos, boolean isRaining, int chunkX, int chunkZ) {
        SnowStorms.tryPlaceSnow((ServerLevel)(Object)this, chunkX, chunkZ);
    }
}
