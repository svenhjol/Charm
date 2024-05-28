package svenhjol.charm.feature.casks.client;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.casks.CasksClient;
import svenhjol.charm.feature.casks.common.Networking;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<CasksClient> {
    public Handlers(CasksClient feature) {
        super(feature);
    }

    @SuppressWarnings("unused")
    public void addedToCaskReceived(Player player, Networking.S2CAddedToCask packet) {
        var minecraft = Minecraft.getInstance();

        if (minecraft.level != null) {
            createParticles(minecraft.level, packet.pos());
        }
    }

    public void createParticles(Level level, BlockPos pos) {
        var random = level.getRandom();
        for(int i = 0; i < 10; ++i) {
            var offsetX = random.nextGaussian() * 0.02d;
            var offsetY = random.nextGaussian() * 0.02d;
            var offsetZ = random.nextGaussian() * 0.02d;

            level.addParticle(ParticleTypes.SMOKE,
                pos.getX() + 0.13 + (0.73d * random.nextFloat()),
                pos.getY() + 0.8d + random.nextFloat() * 0.3d,
                pos.getZ() + 0.13d + (0.73d * random.nextFloat()),
                offsetX, offsetY, offsetZ);
        }
    }
}
