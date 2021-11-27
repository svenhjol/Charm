package svenhjol.charm.module.clear_item_frames;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.init.CharmParticles;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = ClearItemFrames.class)
public class ClearItemFramesClient extends CharmModule {
    @Override
    public void register() {
        ClientPlayNetworking.registerGlobalReceiver(ClearItemFrames.MSG_CLIENT_ADD_AMETHYST, this::handleClientMakeInvisible);
        ClientPlayNetworking.registerGlobalReceiver(ClearItemFrames.MSG_CLIENT_REMOVE_AMETHYST, this::handleClientBreakItemFrame);
    }

    private void handleClientMakeInvisible(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buffer, PacketSender sender) {
        BlockPos pos = BlockPos.of(buffer.readLong());

        client.execute(() -> {
            ClientLevel level = client.level;
            LocalPlayer player = client.player;

            if (level == null || player == null) return;

            level.playSound(player, pos, SoundEvents.SMALL_AMETHYST_BUD_PLACE, SoundSource.PLAYERS, 1.0F, 1.0F);

            for (int i = 0; i < 3; i++) {
                createParticle(level, pos);
            }
        });
    }

    private void handleClientBreakItemFrame(Minecraft client, ClientPacketListener handler, FriendlyByteBuf buffer, PacketSender sender) {
        BlockPos pos = BlockPos.of(buffer.readLong());

        client.execute(() -> {
            ClientLevel level = client.level;
            LocalPlayer player = client.player;

            if (level == null || player == null) return;

            level.playSound(player, pos, SoundEvents.SMALL_AMETHYST_BUD_BREAK, SoundSource.PLAYERS, 1.0F, 1.0F);

            for (int i = 0; i < 3; i++) {
                createParticle(level, pos);
            }
        });
    }

    private void createParticle(ClientLevel level, BlockPos pos) {
        SimpleParticleType particleType = CharmParticles.APPLY_PARTICLE;

        float[] col = DyeColor.PURPLE.getTextureDiffuseColors();
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + 0.5D;
        double z = (double) pos.getZ() + 0.5D;

        level.addAlwaysVisibleParticle(particleType, x, y, z, col[0], col[1], col[2]);
    }
}
