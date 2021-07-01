package svenhjol.charm.module.potion_of_spelunking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.DyeColor;
import svenhjol.charm.init.CharmParticles;
import svenhjol.charm.loader.ClientModule;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@svenhjol.charm.annotation.ClientModule(module = PotionOfSpelunking.class)
public class PotionOfSpelunkingClient extends ClientModule {
    @Override
    public void register() {
        ClientPlayNetworking.registerGlobalReceiver(PotionOfSpelunking.MSG_CLIENT_HAS_EFFECT, this::handleClientHasEffect);
    }

    private void handleClientHasEffect(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        List<BlockPos> positions = Arrays.stream(data.readLongArray()).boxed().map(BlockPos::of).collect(Collectors.toList());
        List<DyeColor> colors = Arrays.stream(data.readVarIntArray()).boxed().map(DyeColor::byId).collect(Collectors.toList());

        client.execute(() -> {
            LocalPlayer player = client.player;
            ClientLevel world = client.level;

            if (player == null || world == null)
                return;

            BlockPos playerPos = player.blockPosition();

            for (int i = 0; i < positions.size(); i++) {
                BlockPos foundPos = positions.get(i);
                DyeColor foundColor = colors.get(i);
                float[] col = foundColor.getTextureDiffuseColors();

                BlockPos test = new BlockPos(foundPos.getX(), playerPos.getY(), foundPos.getZ());

                for (int j = -10; j < 5; j++) {
                    BlockPos to = test.relative(Axis.Y, j);
                    if (!world.getBlockState(to).isCollisionShapeFullBlock(world, to) && world.getBlockState(to.below()).isCollisionShapeFullBlock(world, to.below())) {
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
