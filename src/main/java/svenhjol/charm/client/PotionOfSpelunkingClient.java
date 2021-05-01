package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.init.CharmParticles;
import svenhjol.charm.module.PotionOfSpelunking;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.util.math.Direction.Axis;

public class PotionOfSpelunkingClient extends CharmClientModule {
    public PotionOfSpelunkingClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ClientPlayNetworking.registerGlobalReceiver(PotionOfSpelunking.MSG_CLIENT_HAS_EFFECT, this::handleClientHasEffect);
    }

    private void handleClientHasEffect(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        List<BlockPos> positions = Arrays.stream(data.readLongArray()).boxed().map(BlockPos::fromLong).collect(Collectors.toList());
        List<DyeColor> colors = Arrays.stream(data.readIntArray()).boxed().map(DyeColor::byId).collect(Collectors.toList());

        client.execute(() -> {
            ClientPlayerEntity player = client.player;
            ClientWorld world = client.world;

            if (player == null || world == null)
                return;

            BlockPos playerPos = player.getBlockPos();

            for (int i = 0; i < positions.size(); i++) {
                BlockPos foundPos = positions.get(i);
                DyeColor foundColor = colors.get(i);
                float[] col = foundColor.getColorComponents();

                BlockPos test = new BlockPos(foundPos.getX(), playerPos.getY(), foundPos.getZ());

                for (int j = -10; j < 5; j++) {
                    BlockPos to = test.offset(Axis.Y, j);
                    if (!world.getBlockState(to).isFullCube(world, to) && world.getBlockState(to.down()).isFullCube(world, to.down())) {
                        for (int k = 0; k < 2; k++) {
                            int y = to.getY() + k;
                            world.addParticle(CharmParticles.ORE_GLOW_PARTICLE, test.getX() + 0.5D, y - (k * 0.7D), foundPos.getZ() + 0.5D, col[0], col[1], col[2]);
                        }
                    }
                }
            }
        });
    }
}
