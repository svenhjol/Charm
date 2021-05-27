package svenhjol.charm.mixin.callback;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.WorldSaveHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.event.PlayerSaveDataCallback;

@Mixin(PlayerManager.class)
public class PlayerSaveDataCallbackMixin {
    @Shadow
    @Final
    private WorldSaveHandler saveHandler;

    /**
     * Fires the {@link PlayerSaveDataCallback} event.
     */
    @Inject(
        method = "loadPlayerData",
        at = @At("HEAD")
    )
    private void hookLoadPlayerData(ServerPlayerEntity playerEntity, CallbackInfoReturnable<NbtCompound> cir) {
        PlayerSaveDataCallback.EVENT.invoker().interact(playerEntity, PlayerHelper.getPlayerDataDir(saveHandler));
    }
}
