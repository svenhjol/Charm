package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.CharmParticles;
import svenhjol.charm.base.CharmSounds;
import svenhjol.charm.module.Astrolabes;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AstrolabesClient extends CharmClientModule {
    public AstrolabesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ClientPlayNetworking.registerGlobalReceiver(Astrolabes.MSG_CLIENT_SHOW_AXIS_PARTICLES, this::handleClientShowAxisParticles);
    }

    private void handleClientShowAxisParticles(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        boolean playSound = data.readBoolean();
        List<BlockPos> positions = Arrays.stream(data.readLongArray()).boxed().map(BlockPos::fromLong).collect(Collectors.toList());
        client.execute(() -> {
            ClientWorld world = client.world;
            ClientPlayerEntity player = client.player;
            if (world == null || player == null)
                return;

            int dist = 32;
            Random random = world.random;
            boolean isClose = false;

            for (BlockPos pos : positions) {
                double px = Math.abs(pos.getX() - player.getX());
                double py = Math.abs(pos.getY() - player.getY());
                double pz = Math.abs(pos.getZ() - player.getZ());

                if (py <= dist) {
                    if (pz <= dist) {
                        for (int x = -dist; x < dist; x++) {
                            this.createAxisParticle(world, new BlockPos(player.getX() + x, pos.getY(), pos.getZ()), DyeColor.CYAN);
                        }
                        isClose = true;
                    }

                    if (px <= dist) {
                        for (int z = -dist; z < dist; z++) {
                            this.createAxisParticle(world, new BlockPos(pos.getX(), pos.getY(), player.getZ() + z), DyeColor.BLUE);
                        }
                        isClose = true;
                    }
                }

                if (px <= dist && pz <= dist) {
                    for (int y = -dist; y < dist; y++) {
                        this.createAxisParticle(world, new BlockPos(pos.getX(), player.getY() + y, pos.getZ()), DyeColor.PURPLE);
                    }
                    isClose = true;
                }
            }

            if (playSound && isClose)
                world.playSound(player, player.getBlockPos(), CharmSounds.ASTROLABE, SoundCategory.PLAYERS, 0.27F, 0.8F + (0.4F * random.nextFloat()));
        });
    }

    private void createAxisParticle(ClientWorld world, BlockPos pos, DyeColor color) {
        DefaultParticleType particleType = CharmParticles.AXIS_PARTICLE;

        float[] col = color.getColorComponents();
        double x = (double) pos.getX() + 0.5D;
        double y = (double) pos.getY() + 0.5D;
        double z = (double) pos.getZ() + 0.5D;

        world.addImportantParticle(particleType, x, y, z, col[0], col[1], col[2]);
    }
}
