package svenhjol.charm.module.storage_crates;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

import javax.annotation.Nullable;
import java.util.Random;

public class StorageCratesClient extends CharmClientModule {
    public static long lastEffectTime;

    public StorageCratesClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        StorageCrates.STORAGE_CRATE_BLOCKS.forEach((material, block) -> {
            BlockRenderLayerMap.INSTANCE.putBlock(block, RenderType.cutout());
        });

        BlockEntityRendererRegistry.INSTANCE.register(StorageCrates.BLOCK_ENTITY, StorageCrateBlockEntityRenderer::new);
        ClientPlayNetworking.registerGlobalReceiver(StorageCrates.MSG_CLIENT_UPDATED_CRATE, this::handleInteractedWithCrate);
    }

    private void handleInteractedWithCrate(Minecraft client, ClientPacketListener handler, FriendlyByteBuf data, PacketSender sender) {
        StorageCrates.ActionType actionType = data.readEnum(StorageCrates.ActionType.class);
        BlockPos pos = BlockPos.of(data.readLong());

        client.execute(() -> {
            ClientHelper.getWorld().ifPresent(world -> {
                if (world.getGameTime() - lastEffectTime > 5) {
                    lastEffectTime = world.getGameTime();
                    switch (actionType) {
                        case ADDED -> createEffect(world, pos, ParticleTypes.COMPOSTER, SoundEvents.COMPOSTER_FILL);
                        case REMOVED -> createEffect(world, pos, ParticleTypes.SMOKE, SoundEvents.ITEM_PICKUP);
                        case FILLED -> createEffect(world, pos, ParticleTypes.ASH, null);
                    }
                }
            });
        });
    }

    private void createEffect(Level world, BlockPos pos, ParticleOptions effect, @Nullable SoundEvent sound) {
        if (sound != null)
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), sound, SoundSource.BLOCKS, 1.0F, 1.0F, false);

        Direction.Axis axis;
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof StorageCrateBlock) {
            axis = state.getValue(StorageCrateBlock.FACING).getAxis();
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
