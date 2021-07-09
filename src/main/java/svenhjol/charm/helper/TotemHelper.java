package svenhjol.charm.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * @version 1.0.0-charm
 */
@SuppressWarnings("UnusedReturnValue")
public class TotemHelper {
    public static boolean destroy(Player player, ItemStack totem) {
        if (player.isSpectator() || player.isCreative())
            return false;

        totem.shrink(1);

        if (player.level.isClientSide) {
            effectDestroyTotem(player.blockPosition());
        } else {
            player.level.playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.8F, 1.0F);
        }

        return true;
    }

    @Environment(EnvType.CLIENT)
    public static void effectDestroyTotem(BlockPos pos) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null)
            return;

        double spread = 1.5D;
        for (int i = 0; i < 4; i++) {
            double px = pos.getX() + 0.5D + (Math.random() - 0.5D) * spread;
            double py = pos.getY() + 0.5D + (Math.random() - 0.5D) * spread;
            double pz = pos.getZ() + 0.5D + (Math.random() - 0.5D) * spread;
            mc.level.addParticle(ParticleTypes.LARGE_SMOKE, px, py, pz, 0.0D, 0.1D, 0.0D);
        }
    }
}
