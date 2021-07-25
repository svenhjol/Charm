package svenhjol.charm.mixin.event;

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
import svenhjol.charm.event.PlayerLoadDataCallback;
import svenhjol.charm.helper.PlayerHelper;

@Mixin(PlayerList.class)
public class PlayerLoadDataMixin {
    @Shadow @Final
    private PlayerDataStorage playerIo;

    /**
     * Fires the {@link PlayerLoadDataCallback} event.
     */
    @Inject(
        method = "load",
        at = @At("HEAD")
    )
    private void hookLoadPlayerData(ServerPlayer serverPlayer, CallbackInfoReturnable<CompoundTag> cir) {
        PlayerLoadDataCallback.EVENT.invoker().interact(serverPlayer, PlayerHelper.getPlayerDataDir(playerIo));
    }
}
