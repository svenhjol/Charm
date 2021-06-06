package svenhjol.charm.module.casks;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.casks.CaskBlockEntityRenderer;
import svenhjol.charm.module.casks.Casks;

import java.util.Random;

public class CasksClient extends CharmClientModule {
    public CasksClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ClientPlayNetworking.registerGlobalReceiver(svenhjol.charm.module.casks.Casks.MSG_CLIENT_ADDED_TO_CASK, this::handleClientAddedToCask);
        BlockEntityRendererRegistry.INSTANCE.register(Casks.BLOCK_ENTITY, CaskBlockEntityRenderer::new);
    }

    private void handleClientAddedToCask(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.of(data.readLong());
        client.execute(() -> {
            if (client.level != null)
                createParticles(client.level, pos);
        });
    }

    private void createParticles(Level world, BlockPos pos) {
        Random random = world.getRandom();
        for(int i = 0; i < 10; ++i) {
            double g = random.nextGaussian() * 0.02D;
            double h = random.nextGaussian() * 0.02D;
            double j = random.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.13 + (0.73D * (double)random.nextFloat()), (double)pos.getY() + 0.8D + (double)random.nextFloat() * 0.3D, (double)pos.getZ() + 0.13D + (0.73 * (double)random.nextFloat()), g, h, j);
        }
    }
}
