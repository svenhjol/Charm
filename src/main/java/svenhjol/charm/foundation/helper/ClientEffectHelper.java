package svenhjol.charm.foundation.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;

@SuppressWarnings("unused")
public final class ClientEffectHelper {
    public static void destroyTotem(BlockPos pos) {
        var minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) return;

        var spread = 1.5d;
        for (int i = 0; i < 10; i++) {
            var px = pos.getX() + 0.5d + (Math.random() - 0.5d) * spread;
            var py = pos.getY() + 0.5d + (Math.random() - 0.5d) * spread;
            var pz = pos.getZ() + 0.5d + (Math.random() - 0.5d) * spread;
            minecraft.level.addParticle(ParticleTypes.LARGE_SMOKE, px, py, pz, 0.0d, 0.1d, 0.0d);
        }
    }
}
