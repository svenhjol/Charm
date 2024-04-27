package svenhjol.charm.feature.clear_item_frames;

import net.minecraft.client.particle.ParticleEngine.SpriteParticleRegistration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Networking;
import svenhjol.charm.foundation.Registration;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.common.CommonFeature;

import java.util.Optional;
import java.util.function.Supplier;

public class ClearItemFramesClient extends ClientFeature {
    static Supplier<SpriteParticleRegistration<SimpleParticleType>> particle;

    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return ClearItemFrames.class;
    }

    @Override
    public Optional<Registration<? extends Feature>> registration() {
        return Optional.of(new ClientRegistration(this));
    }

    @Override
    public Optional<Networking<? extends Feature>> networking() {
        return Optional.of(new ClientNetworking(this));
    }

    static void handleAddToItemFrame(Player player, CommonNetworking.AddAmethyst packet) {
        var pos = packet.getPos();
        var sound = packet.getSound();
        player.level().playSound(player, pos, sound, SoundSource.PLAYERS, 1.0f, 1.0f);

        for (int i = 0; i < 3; i++) {
            ClearItemFramesClient.createParticle(player.level(), pos);
        }
    }

    static void handleRemoveFromItemFrame(Player player, CommonNetworking.RemoveAmethyst message) {
        var pos = message.getPos();
        var sound = message.getSound();
        player.level().playSound(player, pos, sound, SoundSource.PLAYERS, 1.0f, 1.0f);

        for (int i = 0; i < 3; i++) {
            ClearItemFramesClient.createParticle(player.level(), pos);
        }
    }

    static void createParticle(Level level, BlockPos pos) {
        var particleType = ClearItemFrames.particleType.get();

        float[] col = DyeColor.PURPLE.getTextureDiffuseColors();
        var x = (double) pos.getX() + 0.5d;
        var y = (double) pos.getY() + 0.5d;
        var z = (double) pos.getZ() + 0.5d;

        level.addAlwaysVisibleParticle(particleType, x, y, z, col[0], col[1], col[2]);
    }
}
