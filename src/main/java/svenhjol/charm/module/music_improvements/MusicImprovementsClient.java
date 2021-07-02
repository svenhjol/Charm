package svenhjol.charm.module.music_improvements;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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
import svenhjol.charm.CharmClient;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.event.PlaySoundCallback;
import svenhjol.charm.helper.DimensionHelper;
import svenhjol.charm.helper.SoundHelper;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@SuppressWarnings("unused")
@ClientModule(module = MusicImprovements.class)
public class MusicImprovementsClient extends CharmModule {
    private SoundInstance musicToStop = null;
    private int ticksBeforeStop = 0;
    private static final List<MusicCondition> musicConditions = new ArrayList<>();

    public static boolean isEnabled;

    @Override
    public void runWhenEnabled() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
        PlaySoundCallback.EVENT.register(this::handlePlaySound);
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);

        if (MusicImprovements.playCreativeMusic) {
            getMusicConditions().add(new MusicCondition(
                SoundEvents.MUSIC_CREATIVE, 1200, 3600, mc -> mc.player != null
                && (!mc.player.isCreative() || !mc.player.isSpectator())
                && DimensionHelper.isOverworld(mc.player.level)
                && new Random().nextFloat() < 0.25F
            ));
        }

        // set statically so hooks can check this is enabled
        isEnabled = true;
    }

    private InteractionResult handleUseBlock(Player player, Level world, InteractionHand hand, BlockHitResult hitResult) {
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
            if (state.getBlock() == Blocks.JUKEBOX && !state.getValue(JukeboxBlock.HAS_RECORD))
                SoundHelper.getSoundManager().stop(null, SoundSource.MUSIC);
        }
    }

    public void checkShouldStopMusic(SoundInstance sound) {
        if (sound.getSource() == SoundSource.MUSIC) {
            // check if there are any records playing
            SoundHelper.getPlayingSounds().forEach((category, s) -> {
                if (category == SoundSource.RECORDS) {
                    musicToStop = sound;
                    CharmClient.LOG.debug("[Music Improvements] Triggered background music while music disc playing");
                }
            });
        }
    }

    public void checkActuallyStopMusic() {
        if (musicToStop != null
            && ++ticksBeforeStop % 10 == 0
        ) {
            SoundHelper.getSoundManager().stop(musicToStop);
            ticksBeforeStop = 0;
            musicToStop = null;
        }
    }

    @Nullable
    public static Music getMusic() {
        for (MusicCondition c : musicConditions) {
            if (c.handle())
                return c.getMusic();
        }

        return null;
    }

    public static List<MusicCondition> getMusicConditions() {
        return musicConditions;
    }

    public static class MusicCondition {
        private final SoundEvent sound;
        private final int minDelay;
        private final int maxDelay;
        private Predicate<Minecraft> condition;

        public MusicCondition(SoundEvent sound, int minDelay, int maxDelay, Predicate<Minecraft> condition) {
            this.sound = sound;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
            this.condition = condition;
        }

        public MusicCondition(Music music) {
            this.sound = music.getEvent();
            this.minDelay = music.getMinDelay();
            this.maxDelay = music.getMaxDelay();
        }

        public boolean handle() {
            if (condition == null) return false;
            return condition.test(Minecraft.getInstance());
        }

        public Music getMusic() {
            return new Music(sound, minDelay, maxDelay, false);
        }

        public SoundEvent getSound() {
            return sound;
        }

        public int getMaxDelay() {
            return maxDelay;
        }

        public int getMinDelay() {
            return minDelay;
        }
    }
}
