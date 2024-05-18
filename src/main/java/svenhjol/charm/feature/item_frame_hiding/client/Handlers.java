package svenhjol.charm.feature.item_frame_hiding.client;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.item_frame_hiding.ItemFrameHidingClient;
import svenhjol.charm.feature.item_frame_hiding.common.Networking;
import svenhjol.charm.foundation.feature.FeatureHolder;

public final class Handlers extends FeatureHolder<ItemFrameHidingClient> {
    public Handlers(ItemFrameHidingClient feature) {
        super(feature);
    }

    public void addToItemFrame(Player player, Networking.AddAmethyst packet) {
        var pos = packet.getPos();
        var sound = packet.getSound();
        player.level().playSound(player, pos, sound, SoundSource.PLAYERS, 1.0f, 1.0f);

        for (int i = 0; i < 3; i++) {
            createParticle(player.level(), pos);
        }
    }

    public void removeFromItemFrame(Player player, Networking.RemoveAmethyst message) {
        var pos = message.getPos();
        var sound = message.getSound();
        player.level().playSound(player, pos, sound, SoundSource.PLAYERS, 1.0f, 1.0f);

        for (int i = 0; i < 3; i++) {
            createParticle(player.level(), pos);
        }
    }

    public void createParticle(Level level, BlockPos pos) {
        var particleType = feature().common().registers.particleType.get();

        float[] col = DyeColor.PURPLE.getTextureDiffuseColors();
        var x = (double) pos.getX() + 0.5d;
        var y = (double) pos.getY() + 0.5d;
        var z = (double) pos.getZ() + 0.5d;

        level.addParticle(particleType, x, y, z, col[0], col[1], col[2]);
    }
}
