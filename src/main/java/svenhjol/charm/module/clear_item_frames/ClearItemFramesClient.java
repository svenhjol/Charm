package svenhjol.charm.module.clear_item_frames;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.init.CharmParticles;

public class ClearItemFramesClient extends CharmClientModule {
    public ClearItemFramesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ClientPlayNetworking.registerGlobalReceiver(ClearItemFrames.MSG_CLIENT_ADD_AMETHYST, this::handleClientMakeInvisible);
        ClientPlayNetworking.registerGlobalReceiver(ClearItemFrames.MSG_CLIENT_REMOVE_AMETHYST, this::handleClientBreakItemFrame);
    }

    private void handleClientMakeInvisible(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());

        client.execute(() -> {
            ClientWorld world = client.world;
            ClientPlayerEntity player = client.player;

            if (world == null || player == null)
                return;

            world.playSound(player, pos, SoundEvents.BLOCK_SMALL_AMETHYST_BUD_PLACE, SoundCategory.PLAYERS, 1.0F, 1.0F);

            for (int i = 0; i < 3; i++) {
                createParticle(world, pos);
            }
        });
    }

    private void handleClientBreakItemFrame(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());

        client.execute(() -> {
            ClientWorld world = client.world;
            ClientPlayerEntity player = client.player;

            if (world == null || player == null)
                return;

            world.playSound(player, pos, SoundEvents.BLOCK_SMALL_AMETHYST_BUD_BREAK, SoundCategory.PLAYERS, 1.0F, 1.0F);

            for (int i = 0; i < 3; i++) {
                createParticle(world, pos);
            }
        });
    }

    private void createParticle(ClientWorld world, BlockPos pos) {
        DefaultParticleType particleType = CharmParticles.APPLY_PARTICLE;

        float[] col = DyeColor.PURPLE.getColorComponents();
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + 0.5D;
        double z = (double) pos.getZ() + 0.5D;

        world.addImportantParticle(particleType, x, y, z, col[0], col[1], col[2]);
    }
}
