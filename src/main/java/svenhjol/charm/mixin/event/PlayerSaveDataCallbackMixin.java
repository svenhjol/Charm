package svenhjol.charm.mixin.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.storage.PlayerDataStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.PlayerSaveDataEvent;
import svenhjol.charm.helper.PlayerHelper;

@Mixin(PlayerList.class)
public class PlayerSaveDataCallbackMixin {
    @Shadow @Final
    private PlayerDataStorage playerIo;

    /**
     * Fires the {@link PlayerSaveDataEvent} event.
     */
    @Inject(
        method = "save",
        at = @At("HEAD")
    )
    private void hookLoadPlayerData(ServerPlayer serverPlayer, CallbackInfo ci) {
        PlayerSaveDataEvent.EVENT.invoker().interact(serverPlayer, PlayerHelper.getPlayerDataDir(playerIo));
    }
}
