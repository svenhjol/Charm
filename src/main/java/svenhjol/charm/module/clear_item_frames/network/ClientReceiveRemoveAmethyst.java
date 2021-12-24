package svenhjol.charm.module.clear_item_frames.network;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import svenhjol.charm.module.clear_item_frames.ClearItemFramesClient;
import svenhjol.charm.network.ClientReceiver;
import svenhjol.charm.network.Id;

@Id("charm:remove_amethyst_from_item_frame")
public class ClientReceiveRemoveAmethyst extends ClientReceiver {
    @Override
    public void handle(Minecraft client, FriendlyByteBuf buffer) {
        if (client.level == null || client.player == null) return;
        BlockPos pos = BlockPos.of(buffer.readLong());

        client.execute(() -> {
            client.level.playSound(client.player, pos, SoundEvents.SMALL_AMETHYST_BUD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);

            for (int i = 0; i < 3; i++) {
                ClearItemFramesClient.createParticle(client.level, pos);
            }
        });
    }
}
