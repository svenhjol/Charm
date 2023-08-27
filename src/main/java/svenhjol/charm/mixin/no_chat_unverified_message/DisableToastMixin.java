package svenhjol.charm.mixin.no_chat_unverified_message;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPacketListener.class)
public class DisableToastMixin {
    @Redirect(
        method = "handleServerData",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/game/ClientboundServerDataPacket;enforcesSecureChat()Z",
            ordinal = 1
        )
    )
    private boolean hookStopNaggingMe(ClientboundServerDataPacket instance) {
        return true; // Flip the condition.
    }
}
