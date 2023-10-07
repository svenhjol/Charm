package svenhjol.charm.feature.clear_item_frames;

import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.function.Supplier;

@ClientFeature(mod = CharmClient.MOD_ID, feature = ClearItemFrames.class)
public class ClearItemFramesClient extends CharmonyFeature {
    static Supplier<SpriteParticleRegistration<SimpleParticleType>> particle;

    @Override
    public void register() {
        particle = CharmClient.instance().registry().particle(ClearItemFrames.particleType,
            () -> ApplyAmethystClientParticle::new);
    }

    static void handleItemFrameInteraction(ClearItemFramesNetwork.IItemFrameInteraction message, Player player) {
        var pos = message.getPos();
        var sound = message.getSound();
        player.level().playSound(player, pos, sound, SoundSource.PLAYERS, 1.0F, 1.0F);

        for (int i = 0; i < 3; i++) {
            ClearItemFramesClient.createParticle(player.level(), pos);
        }
    }

    static void createParticle(Level level, BlockPos pos) {
        var particleType = ClearItemFrames.particleType.get();

        float[] col = DyeColor.PURPLE.getTextureDiffuseColors();
        var x = (double) pos.getX() + 0.5D;
        var y = (double) pos.getY() + 0.5D;
        var z = (double) pos.getZ() + 0.5D;

        level.addAlwaysVisibleParticle(particleType, x, y, z, col[0], col[1], col[2]);
    }
}
