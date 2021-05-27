package svenhjol.charm.module.storage_crates;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.ClientHelper;

import javax.annotation.Nullable;
import java.util.Random;

public class StorageCratesClient extends CharmClientModule {
    public StorageCratesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        StorageCrates.STORAGE_CRATE_BLOCKS.forEach((material, block) -> {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderLayer.getCutout());
        });

        BlockEntityRendererRegistry.INSTANCE.register(StorageCrates.BLOCK_ENTITY, StorageCrateBlockEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(StorageCrates.MSG_CLIENT_UPDATED_CRATE, this::handleInteractedWithCrate);
    }

    private void handleInteractedWithCrate(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender sender) {
        StorageCrates.ActionType actionType = data.readEnumConstant(StorageCrates.ActionType.class);
        BlockPos pos = BlockPos.fromLong(data.readLong());

        client.execute(() -> {
            ClientHelper.getWorld().ifPresent(world -> {
                switch (actionType) {
                    case ADDED:
                        createEffect(world, pos, ParticleTypes.COMPOSTER, SoundEvents.BLOCK_COMPOSTER_FILL);
                        break;

                    case REMOVED:
                        createEffect(world, pos, ParticleTypes.SMOKE, SoundEvents.ENTITY_ITEM_PICKUP);
                        break;

                    case FILLED:
                        createEffect(world, pos, ParticleTypes.ASH, null);
                        break;
                }
            });
        });
    }

    private void createEffect(World world, BlockPos pos, ParticleEffect effect, @Nullable SoundEvent sound) {
        if (sound != null)
            world.playSound(pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.BLOCKS, 1.0F, 1.0F, false);

        Direction.Axis axis;
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof StorageCrateBlock) {
            axis = state.get(StorageCrateBlock.FACING).getAxis();
        } else {
            axis = Direction.Axis.Y;
        }

        double d = axis.isVertical() ? 0.05D : 0.0D;
        double e = axis.isVertical() ? 0.13D : 0.32D;
        double f = axis.isVertical() ? 0.74D : 0.45D;
        Random random = world.getRandom();

        for(int i = 0; i < 8; ++i) {
            double g = random.nextGaussian() * 0.02D;
            double h = random.nextGaussian() * 0.02D;
            double j = random.nextGaussian() * 0.02D;
            world.addParticle(effect, (double)pos.getX() + e + f * (double)random.nextFloat(), (double)pos.getY() + d + (double)random.nextFloat() * (0.75D - d), (double)pos.getZ() + e + f * (double)random.nextFloat(), g, h, j);
        }
    }
}
