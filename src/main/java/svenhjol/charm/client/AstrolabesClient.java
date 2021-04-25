package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.CharmParticles;
import svenhjol.charm.module.Astrolabes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AstrolabesClient extends CharmClientModule {
    public AstrolabesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ClientPlayNetworking.registerGlobalReceiver(Astrolabes.MSG_CLIENT_SHOW_AXIS_PARTICLES, this::handleClientShowAxisParticles);
//        FabricModelPredicateProviderRegistry.register(Astrolabes.ASTROLABE_OLD, new Identifier(Charm.MOD_ID, "aligned_x"), this::handleAlignX);
//        FabricModelPredicateProviderRegistry.register(Astrolabes.ASTROLABE_OLD, new Identifier(Charm.MOD_ID, "aligned_y"), this::handleAlignY);
//        FabricModelPredicateProviderRegistry.register(Astrolabes.ASTROLABE_OLD, new Identifier(Charm.MOD_ID, "aligned_z"), this::handleAlignZ);
    }

    private void handleClientShowAxisParticles(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        List<BlockPos> positions = Arrays.stream(data.readLongArray()).boxed().map(BlockPos::fromLong).collect(Collectors.toList());
        client.execute(() -> {
            ClientWorld world = client.world;
            ClientPlayerEntity player = client.player;
            if (world == null || player == null)
                return;

            int dist = 32;

            for (BlockPos pos : positions) {
                double px = Math.abs(pos.getX() - player.getX());
                double py = Math.abs(pos.getY() - player.getY());
                double pz = Math.abs(pos.getZ() - player.getZ());

                if (py <= dist) {
                    if (pz <= dist) {
                        for (int x = -dist; x < dist; x++) {
                            this.createAxisParticles(world, new BlockPos(player.getX() + x, pos.getY(), pos.getZ()), DyeColor.CYAN);
                        }
                    }

                    if (px <= dist) {
                        for (int z = -dist; z < dist; z++) {
                            this.createAxisParticles(world, new BlockPos(pos.getX(), pos.getY(), player.getZ() + z), DyeColor.BLUE);
                        }
                    }
                }

                if (px <= dist && pz <= dist) {
                    for (int y = -dist; y < dist; y++) {
                        this.createAxisParticles(world, new BlockPos(pos.getX(), player.getY() + y, pos.getZ()), DyeColor.PURPLE);
                    }
                }

            }
        });
    }

    private void createAxisParticles(ClientWorld world, BlockPos pos, DyeColor color) {
        DefaultParticleType particleType = CharmParticles.AXIS_PARTICLE;

        float[] col = color.getColorComponents();
        for (int i = 0; i < 4; i++) {
            double x = (double) pos.getX() + 0.5D;
            double y = (double) pos.getY() + 0.5D;
            double z = (double) pos.getZ() + 0.5D;

            world.addImportantParticle(particleType, x, y, z, col[0], col[1], col[2]);
        }
    }

//    private float handleAlignX(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
//        return isAligned(stack, world, entity, pos
//            -> pos.getZ() == entity.getBlockZ()) ? 1 : 0;
//    }
//
//    private float handleAlignY(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
//        return isAligned(stack, world, entity, pos
//            -> pos.getY() == entity.getBlockY()) ? 1 : 0;
//    }
//
//    private float handleAlignZ(ItemStack stack, ClientWorld world, LivingEntity entity, int seed) {
//        return isAligned(stack, world, entity, pos ->
//            pos.getX() == entity.getBlockX()) ? 1 : 0;
//    }

//    private boolean isAligned(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, Predicate<BlockPos.Mutable> on) {
//        if (entity == null || entity.world == null)
//            return false;
//
//        Identifier dimension = AstrolabeBlockItem.getDimension(stack);
//        BlockPos pos = AstrolabeBlockItem.getPosition(stack);
//        if (pos == null)
//            return false;
//
//        BlockPos.Mutable dimensionPos = Astrolabes.getDimensionPosition(entity.world, pos, dimension);
//        return on.test(dimensionPos);
//    }
}
