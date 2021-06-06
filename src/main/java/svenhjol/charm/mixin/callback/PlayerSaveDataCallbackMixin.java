package svenhjol.charm.mixin.callback;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.helper.PlayerHelper;
import svenhjol.charm.event.PlayerSaveDataCallback;

@Mixin(PlayerList.class)
public class PlayerSaveDataCallbackMixin {
    @Shadow
    @Final
    private PlayerDataStorage saveHandler;

    /**
     * Fires the {@link PlayerSaveDataCallback} event.
     */
    @Inject(
        method = "loadPlayerData",
        at = @At("HEAD")
    )
    private void hookLoadPlayerData(ServerPlayer playerEntity, CallbackInfoReturnable<CompoundTag> cir) {
        PlayerSaveDataCallback.EVENT.invoker().interact(playerEntity, PlayerHelper.getPlayerDataDir(saveHandler));
    }
}
