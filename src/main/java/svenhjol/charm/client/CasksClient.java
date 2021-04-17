package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.Casks;

import java.util.Random;

public class CasksClient extends CharmClientModule {
    public CasksClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ClientPlayNetworking.registerGlobalReceiver(Casks.MSG_CLIENT_ADDED_TO_CASK, this::handleClientAddedToCask);
    }

    private void handleClientAddedToCask(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        BlockPos pos = BlockPos.fromLong(data.readLong());
        client.execute(() -> effectAddItem(pos));
    }

    private void effectAddItem(BlockPos pos) {
        ClientWorld world = MinecraftClient.getInstance().world;
        if (world == null)
            return;

        Random random = world.getRandom();

        for(int i = 0; i < 10; ++i) {
            double g = random.nextGaussian() * 0.02D;
            double h = random.nextGaussian() * 0.02D;
            double j = random.nextGaussian() * 0.02D;
            world.addParticle(ParticleTypes.SMOKE, (double)pos.getX() + 0.13 + (0.73D * (double)random.nextFloat()), (double)pos.getY() + 0.8D + (double)random.nextFloat() * 0.3D, (double)pos.getZ() + 0.13D + (0.73 * (double)random.nextFloat()), g, h, j);
        }
    }
}
