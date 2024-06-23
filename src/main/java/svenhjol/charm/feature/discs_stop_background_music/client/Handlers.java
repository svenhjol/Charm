package svenhjol.charm.feature.discs_stop_background_music.client;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.feature.discs_stop_background_music.DiscsStopBackgroundMusic;

@SuppressWarnings("unused")
public final class Handlers extends FeatureHolder<DiscsStopBackgroundMusic> {
    private static final int CHECK_TICKS = 10;
    public SoundInstance musicToStop = null;
    public int ticksBeforeStop = 0;

    public Handlers(DiscsStopBackgroundMusic feature) {
        super(feature);
    }

    public void clientTick(Minecraft minecraft) {
        tryStopMusic();
    }

    public void soundPlay(SoundEngine soundEngine, SoundInstance soundInstance) {
        checkMusicToStop(soundInstance);
    }

    public InteractionResult blockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        checkMusicToStop(level, hitResult.getBlockPos(), player.getItemInHand(hand));
        return InteractionResult.PASS;
    }

    public void checkMusicToStop(Level level, BlockPos pos, ItemStack stack) {
        if (level.isClientSide() && stack.has(DataComponents.JUKEBOX_PLAYABLE)) {
            var state = level.getBlockState(pos);
            if (state.getBlock() == Blocks.JUKEBOX && !state.getValue(JukeboxBlock.HAS_RECORD)) {
                getSoundManager().stop(null, SoundSource.MUSIC);
            }
        }
    }

    public void checkMusicToStop(SoundInstance sound) {
        if (sound.getSource() == SoundSource.MUSIC) {
            // check if there are any records playing
            getPlayingSounds().forEach((category, s) -> {
                if (category == SoundSource.RECORDS) {
                    musicToStop = sound;
                    feature().log().debug("Sound source check music: Background music suppressed");
                }
            });
        }
        if (sound.getSource() == SoundSource.RECORDS) {
            getPlayingSounds().forEach((category, s) -> {
                if (category == SoundSource.MUSIC) {
                    musicToStop = s;
                    feature().log().debug("Sound source check record: Background music suppressed");
                }
            });
        }
    }

    public void tryStopMusic() {
        if (musicToStop != null && ++ticksBeforeStop % CHECK_TICKS == 0) {
            getSoundManager().stop(musicToStop);
            ticksBeforeStop = 0;
            musicToStop = null;
        }
    }


    SoundManager getSoundManager() {
        return Minecraft.getInstance().getSoundManager();
    }

    Multimap<SoundSource, SoundInstance> getPlayingSounds() {
        var soundEngine = getSoundManager().soundEngine;
        return soundEngine.instanceBySource;
    }
}
