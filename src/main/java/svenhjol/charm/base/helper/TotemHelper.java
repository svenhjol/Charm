package svenhjol.charm.base.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class TotemHelper {
    public static boolean destroy(PlayerEntity player, ItemStack totem) {
        if (player.isSpectator() || player.isCreative())
            return false;

        totem.decrement(1);

        if (player.world.isClient) {
            effectDestroyTotem(player.getBlockPos());
        } else {
            player.world.playSound(null, player.getBlockPos(), SoundEvents.ITEM_TOTEM_USE, SoundCategory.PLAYERS, 0.8F, 1.0F);
        }

        return true;
    }

    @Environment(EnvType.CLIENT)
    public static void effectDestroyTotem(BlockPos pos) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.world == null || mc.player == null)
            return;

        double spread = 1.5D;
        for (int i = 0; i < 4; i++) {
            double px = pos.getX() + 0.5D + (Math.random() - 0.5D) * spread;
            double py = pos.getY() + 0.5D + (Math.random() - 0.5D) * spread;
            double pz = pos.getZ() + 0.5D + (Math.random() - 0.5D) * spread;
            mc.world.addParticle(ParticleTypes.LARGE_SMOKE, px, py, pz, 0.0D, 0.1D, 0.0D);
        }
    }
}
