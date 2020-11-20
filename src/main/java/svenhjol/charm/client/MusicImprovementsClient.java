package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import svenhjol.charm.CharmClient;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.DimensionHelper;
import svenhjol.charm.base.helper.SoundHelper;
import svenhjol.charm.event.PlaySoundCallback;
import svenhjol.charm.module.MusicImprovements;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@SuppressWarnings("unused")
public class MusicImprovementsClient extends CharmClientModule {
    private SoundInstance musicToStop = null;
    private int ticksBeforeStop = 0;
    private static SoundInstance currentMusic;
    private static Identifier currentDim = null;
    private static int timeUntilNextMusic = 100;
    private static final List<MusicCondition> musicConditions = new ArrayList<>();
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

        UseBlockCallback.EVENT.register(this::handleUseBlock);
        PlaySoundCallback.EVENT.register(this::handlePlaySound);
        ClientTickEvents.END_CLIENT_TICK.register(this::handleClientTick);
    }

    private ActionResult handleUseBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        stopRecord(player, hitResult.getBlockPos(), player.getStackInHand(hand));
        return ActionResult.PASS;
    }

    private void handlePlaySound(SoundSystem soundSystem, SoundInstance sound) {
        checkShouldStopMusic(sound);
    }

    private void handleClientTick(MinecraftClient client) {
        checkActuallyStopMusic();
    }

    public void addCreativeMusicCondition() {
        musicConditions.add(new MusicCondition(
            SoundEvents.MUSIC_CREATIVE, 1200, 3600, mc -> mc.player != null
                && (!mc.player.isCreative() || !mc.player.isSpectator())
                && DimensionHelper.isDimension(mc.player.world, new Identifier("overworld"))
                && new Random().nextFloat() < 0.25F
        ));
    }

    public void stopRecord(Entity entity, BlockPos pos, ItemStack stack) {
        if (entity.world.isClient
            && entity instanceof PlayerEntity
            && stack.getItem() instanceof MusicDiscItem
        ) {
            BlockState state = entity.world.getBlockState(pos);
            if (state.getBlock() == Blocks.JUKEBOX && !state.get(JukeboxBlock.HAS_RECORD))
                SoundHelper.getSoundManager().stopSounds(null, SoundCategory.MUSIC);
        }
    }

    public void checkShouldStopMusic(SoundInstance sound) {
        if (sound.getCategory() == SoundCategory.MUSIC) {
            // check if there are any records playing
            SoundHelper.getPlayingSounds().forEach((category, s) -> {
                if (category == SoundCategory.RECORDS) {
                    musicToStop = sound;
                    CharmClient.LOG.debug("Triggered background music while record playing");
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
        MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world == null) return false;
        MusicCondition ambient = getMusicCondition();

        if (currentMusic != null) {
            if (!DimensionHelper.isDimension(mc.world, currentDim))
                forceStop();

            if (!mc.getSoundManager().isPlaying(currentMusic)) {
                currentMusic = null;
                timeUntilNextMusic = Math.min(MathHelper.nextInt(new Random(), ambient.getMinDelay(), 3600), timeUntilNextMusic);
            }
        }

        timeUntilNextMusic = Math.min(timeUntilNextMusic, ambient.getMaxDelay());

        if (currentMusic == null && timeUntilNextMusic-- <= 0) {
            currentDim = DimensionHelper.getDimension(mc.world);
            currentMusic = PositionedSoundInstance.music(ambient.getSound());

            if (currentMusic.getSound() != SoundManager.MISSING_SOUND) {
                mc.getSoundManager().play(currentMusic);
                timeUntilNextMusic = Integer.MAX_VALUE;
            }
        }

        return true;
    }

    public static boolean handleStop() {
        if (currentMusic != null) {
            MinecraftClient.getInstance().getSoundManager().stop(currentMusic);
            currentMusic = null;
            timeUntilNextMusic = 0;
        }
        return true;
    }

    public static boolean handlePlaying(MusicSound music) {
        return currentMusic != null && music.getSound().getId().equals(currentMusic.getId());
    }

    public static void forceStop() {
        MinecraftClient.getInstance().getSoundManager().stop(currentMusic);
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

        // if none available, just play a default background track
        if (condition == null)
            condition = new MusicCondition(MinecraftClient.getInstance().getMusicType());

        return condition;
    }

    public static List<MusicCondition> getMusicConditions() {
        return musicConditions;
    }

    public static class MusicCondition {
        private final SoundEvent sound;
        private final int minDelay;
        private final int maxDelay;
        private Predicate<MinecraftClient> condition;

        public MusicCondition(SoundEvent sound, int minDelay, int maxDelay, Predicate<MinecraftClient> condition) {
            this.sound = sound;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
            this.condition = condition;
        }

        public MusicCondition(MusicSound music) {
            this.sound = music.getSound();
            this.minDelay = music.getMinDelay();
            this.maxDelay = music.getMaxDelay();
        }

        public boolean handle() {
            if (condition == null) return false;
            return condition.test(MinecraftClient.getInstance());
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
