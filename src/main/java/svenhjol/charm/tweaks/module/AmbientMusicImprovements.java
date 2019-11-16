package svenhjol.charm.tweaks.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.JukeboxBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MusicTicker.MusicType;
import net.minecraft.client.audio.SimpleSound;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.MusicDiscItem;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.sound.SoundEvent.SoundSourceEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.base.CharmSounds;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.helper.SoundHelper;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

@Module(mod = Charm.MOD_ID, category = CharmCategories.TWEAKS, hasSubscriptions = true, server = false,
    description = "Expands vanilla ambient music and prevents the background music when playing a music disc in a jukebox.")
public class AmbientMusicImprovements extends MesonModule
{
    @Config(name = "Override delay", description = "Override the delay (in ticks) between ambient music tracks.  If zero, does not override the track default.")
    public static int maxDelayOverride = 3600;

    @Config(name = "Play Creative music", description = "If true, the six Creative music tracks may play in survival mode.")
    public static boolean playCreativeMusic = true;

    @Config(name = "Play Charm music", description = "If true, has a chance of playing Charm music.")
    public static boolean playCharmMusic = true;

    public static List<AmbientMusicCondition> conditions = new ArrayList<>();
    public static ISound currentMusic;
    public static int timeUntilNextMusic = 100;
    private final static Random random;
    private final static Minecraft mc;
    private int ticksBeforeStop;
    private ISound musicToStop = null;

    @Override
    public void setupClient(FMLClientSetupEvent event)
    {
        if (playCharmMusic) {
            conditions.add(new AmbientMusicCondition(CharmSounds.MUSIC_COLD, 1200, 3600, mc -> mc.player != null
                && (!mc.player.isCreative() || !mc.player.isSpectator())
                && mc.player.world.getBiome(new BlockPos(mc.player)).getCategory() == Category.ICY
                && random.nextFloat() < 0.33F
            ));
        }

        if (playCreativeMusic) {
            conditions.add(new AmbientMusicCondition(SoundEvents.MUSIC_CREATIVE, 1200, 3600, mc -> mc.player != null
                && (!mc.player.isCreative() || !mc.player.isSpectator())
                && mc.player.world.dimension instanceof OverworldDimension
                && random.nextFloat() < 0.25F
            ));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onBlockInteract(RightClickBlock event)
    {
        if (event.getWorld().isRemote) {
            BlockState state = event.getWorld().getBlockState(event.getPos());
            if (event.getEntity() instanceof PlayerEntity
                && event.getItemStack().getItem() instanceof MusicDiscItem
                && state.getBlock() == Blocks.JUKEBOX
                && !state.get(JukeboxBlock.HAS_RECORD)
            ) {
                SoundHelper.getSoundHandler().stop(null, SoundCategory.MUSIC);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onSoundPlay(SoundSourceEvent event)
    {
        ISound triggered = event.getSound();
        if (triggered.getCategory() == SoundCategory.MUSIC) {
            // check if there are any records playing
            SoundHelper.getPlayingSounds().forEach((category, sound) -> {
                if (category == SoundCategory.RECORDS) {
                    musicToStop = triggered;
                    Meson.debug("Triggered background music while record playing");
                }
            });
        }
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END
            && musicToStop != null
            && ++ticksBeforeStop % 10 == 0
        ) {
            SoundHelper.getSoundHandler().stop(musicToStop);
            ticksBeforeStop = 0;
            musicToStop = null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean handleTick(@Nullable ISound current)
    {
        AmbientMusicCondition ambient = getAmbientMusicType();

        if (currentMusic != null) {
//            if (!ambient.getSound().getName().equals(currentMusic.getSoundLocation())) {
//                mc.getSoundHandler().stop(currentMusic);
//                currentMusic = null;
//                timeUntilNextMusic = MathHelper.nextInt(random, 0, ambient.getMinDelay() / 2);
//            }

            if (!mc.getSoundHandler().isPlaying(currentMusic)) {
                currentMusic = null;
                int max = maxDelayOverride == 0 ? ambient.getMaxDelay() : maxDelayOverride;
                timeUntilNextMusic = Math.min(MathHelper.nextInt(random, ambient.getMinDelay(), max), timeUntilNextMusic);
            }
        }

        timeUntilNextMusic = Math.min(timeUntilNextMusic, ambient.getMaxDelay());

        if (currentMusic == null && timeUntilNextMusic-- <= 0) {
            currentMusic = SimpleSound.music(ambient.getSound());
            mc.getSoundHandler().play(currentMusic);
            timeUntilNextMusic = Integer.MAX_VALUE;
        }

        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean handleStop()
    {
        if (currentMusic != null) {
            mc.getSoundHandler().stop(currentMusic);
            currentMusic = null;
            timeUntilNextMusic = 0;
        }
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public static boolean handlePlaying(MusicType type)
    {
        return currentMusic != null && type.getSound().getName().equals(currentMusic.getSoundLocation());
    }

    @OnlyIn(Dist.CLIENT)
    public static void forceStop()
    {
        mc.getSoundHandler().stop(currentMusic);
        currentMusic = null;
        timeUntilNextMusic = 3600;
    }

    public static AmbientMusicCondition getAmbientMusicType()
    {
        AmbientMusicCondition condition = null;

        for (AmbientMusicCondition c : conditions) {
            if (c.handle()) {
                condition = c;
                break;
            }
        }

        if (condition == null) {
            condition = new AmbientMusicCondition(Minecraft.getInstance().getAmbientMusicType());
        }

        return condition;
    }

    public static class AmbientMusicCondition
    {
        private SoundEvent sound;
        private int minDelay;
        private int maxDelay;
        private Predicate<Minecraft> condition;
        private static final Minecraft mc;

        public AmbientMusicCondition(SoundEvent sound, int minDelay, int maxDelay, Predicate<Minecraft> condition)
        {
            this.sound = sound;
            this.minDelay = minDelay;
            this.maxDelay = maxDelay;
            this.condition = condition;
        }

        public AmbientMusicCondition(MusicType type)
        {
            this.sound = type.getSound();
            this.minDelay = type.getMinDelay();
            this.maxDelay = type.getMaxDelay();
        }

        public boolean handle()
        {
            if (condition == null) return false;
            return condition.test(mc);
        }

        public SoundEvent getSound()
        {
            return sound;
        }

        public int getMaxDelay()
        {
            return maxDelay;
        }

        public int getMinDelay()
        {
            return minDelay;
        }

        static
        {
            mc = Minecraft.getInstance();
        }
    }

    static
    {
        random = new Random();
        mc = Minecraft.getInstance();
    }
}
