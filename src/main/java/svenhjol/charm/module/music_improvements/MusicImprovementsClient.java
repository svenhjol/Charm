package svenhjol.charm.module.music_improvements;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
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
import svenhjol.charm.event.PlaySoundCallback;
import svenhjol.charm.helper.DimensionHelper;
import svenhjol.charm.helper.SoundHelper;
import svenhjol.charm.mixin.accessor.ChannelAccessAccessor;
import svenhjol.charm.mixin.accessor.SoundEngineAccessor;
import svenhjol.charm.mixin.accessor.SoundManagerAccessor;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.core.Core;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class MusicImprovementsClient extends CharmClientModule {
    private SoundInstance musicToStop = null;
    private int ticksBeforeStop = 0;

    private static SoundInstance currentMusic;
    private static ResourceLocation currentDim = null;
    private static MusicCondition lastCondition;
    private static final List<MusicCondition> musicConditions = new ArrayList<>();
    private static int timeUntilNextMusic = 100;
    private static int debugChannelTicks = 0;

    public static boolean isEnabled;

    public MusicImprovementsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        // set statically so hooks can check this is enabled
        isEnabled = module.enabled;

        if (MusicImprovements.playCreativeMusic)
            addCreativeMusicCondition();
    }

    @Override
    public void init() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
        PlaySoundCallback.EVENT.register(this::handlePlaySound);
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
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

    public void addCreativeMusicCondition() {
        musicConditions.add(new MusicCondition(
            SoundEvents.MUSIC_CREATIVE, 1200, 3600, mc -> mc.player != null
                && (!mc.player.isCreative() || !mc.player.isSpectator())
                && DimensionHelper.isDimension(mc.player.level, Level.OVERWORLD)
                && new Random().nextFloat() < 0.25F
        ));
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

    public static boolean handleTick(SoundInstance current) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) return false;
        if (lastCondition == null)
            lastCondition = getMusicCondition();

        if (currentMusic != null) {
            if (!DimensionHelper.isDimension(mc.level, currentDim)) {
                CharmClient.LOG.debug("[Music Improvements] Stopping music as the dimension is no longer correct");
                forceStop();
                currentMusic = null;
            }

            if (currentMusic != null && !mc.getSoundManager().isActive(currentMusic)) {
                CharmClient.LOG.debug("[Music Improvements] Music has finished, setting currentMusic to null");
                timeUntilNextMusic = Math.min(Mth.nextInt(new Random(), lastCondition.getMinDelay(), 3600), timeUntilNextMusic);
                currentMusic = null;
            }
        }

        timeUntilNextMusic = Math.min(timeUntilNextMusic, lastCondition.getMaxDelay());

        if (currentMusic == null && timeUntilNextMusic-- <= 0) {
            MusicCondition condition = getMusicCondition();
            CharmClient.LOG.debug("[Music Improvements] Selected music condition with sound: " + condition.getSound().getLocation());
            forceStop();

            currentDim = DimensionHelper.getDimension(mc.level);
            currentMusic = SimpleSoundInstance.forMusic(condition.getSound());

            if (currentMusic.getSound() != SoundManager.EMPTY_SOUND) {
                mc.getSoundManager().play(currentMusic);
                lastCondition = condition;
                timeUntilNextMusic = Integer.MAX_VALUE;
            }
        }

        mc.getSoundManager().tick(true);

        if (Core.debug && MusicImprovements.debugActiveChannels && debugChannelTicks++ % 20 == 0) {
            SoundEngine soundEngine = ((SoundManagerAccessor) mc.getSoundManager()).getSoundEngine();
            ChannelAccess channelAccess = ((SoundEngineAccessor) soundEngine).getChannelAccess();
            Set<ChannelAccess.ChannelHandle> channels = ((ChannelAccessAccessor) channelAccess).getChannels();
            CharmClient.LOG.debug("[Music Improvements] Active channels: " + channels.size());
            if (debugChannelTicks >= 999)
                debugChannelTicks = 0;
        }

        return true;
    }

    public static boolean handleStop() {
        if (currentMusic != null) {
            Minecraft.getInstance().getSoundManager().stop(currentMusic);
            currentMusic = null;
            timeUntilNextMusic = lastCondition != null ? new Random().nextInt(Math.min(lastCondition.getMinDelay(), 3600) + 100) + 1000 : timeUntilNextMusic + 100;
            CharmClient.LOG.debug("[Music Improvements] Stop was called, setting timeout to " + timeUntilNextMusic);
        }
        return true;
    }

    public static boolean handlePlaying(Music music) {
        return currentMusic != null && music.getEvent().getLocation().equals(currentMusic.getLocation());
    }

    public static void forceStop() {
        Minecraft.getInstance().getSoundManager().stop(currentMusic);
        currentMusic = null;
        timeUntilNextMusic = 3600;
    }

    public static MusicCondition getMusicCondition() {
        MusicCondition condition = null;

        // select an available condition from the pool of conditions
        for (MusicCondition c : musicConditions) {
            if (c.handle()) {
                condition = c;
                break;
            }
        }

        // if none available, default to vanilla music selection
        if (condition == null)
            condition = new MusicCondition(Minecraft.getInstance().getSituationalMusic());

        return condition;
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
