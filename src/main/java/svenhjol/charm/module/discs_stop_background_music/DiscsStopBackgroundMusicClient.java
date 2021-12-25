package svenhjol.charm.module.discs_stop_background_music;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.api.event.PlaySoundCallback;
import svenhjol.charm.helper.LogHelper;
import svenhjol.charm.helper.SoundHelper;
import svenhjol.charm.loader.CharmModule;

@SuppressWarnings("unused")
@ClientModule(module = DiscsStopBackgroundMusic.class)
public class DiscsStopBackgroundMusicClient extends CharmModule {
    private SoundInstance musicToStop = null;
    private int ticksBeforeStop = 0;

    @Override
    public void runWhenEnabled() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
        PlaySoundCallback.EVENT.register(this::handlePlaySound);
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
    }

    private InteractionResult handleUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        stopRecord(player, hitResult.getBlockPos(), player.getItemInHand(hand));
        return InteractionResult.PASS;
    }

    private void handlePlaySound(SoundEngine soundSystem, SoundInstance sound) {
        checkShouldStopMusic(sound);
    }

    private void handleClientTick(Minecraft client) {
        checkActuallyStopMusic();
    }

    public void stopRecord(Entity entity, BlockPos pos, ItemStack stack) {
        if (entity.level.isClientSide
            && entity instanceof Player
            && stack.getItem() instanceof RecordItem
        ) {
            BlockState state = entity.level.getBlockState(pos);
            if (state.getBlock() == Blocks.JUKEBOX && !state.getValue(JukeboxBlock.HAS_RECORD)) {
                SoundHelper.getSoundManager().stop(null, SoundSource.MUSIC);
            }
        }
    }

    public void checkShouldStopMusic(SoundInstance sound) {
        if (sound.getSource() == SoundSource.MUSIC) {
            // check if there are any records playing
            SoundHelper.getPlayingSounds().forEach((category, s) -> {
                if (category == SoundSource.RECORDS) {
                    musicToStop = sound;
                    LogHelper.debug(getClass(), "Triggered background music while music disc playing");
                }
            });
        }
    }

    public void checkActuallyStopMusic() {
        if (musicToStop != null && ++ticksBeforeStop % 10 == 0) {
            SoundHelper.getSoundManager().stop(musicToStop);
            ticksBeforeStop = 0;
            musicToStop = null;
        }
    }
}
