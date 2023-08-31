package svenhjol.charm.feature.discs_stop_background_music;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
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
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.CharmClient;
import svenhjol.charm.mixin.accessor.SoundEngineAccessor;
import svenhjol.charm.mixin.accessor.SoundManagerAccessor;
import svenhjol.charm_api.event.BlockUseEvent;
import svenhjol.charm_api.event.ClientTickEvent;
import svenhjol.charm_api.event.SoundPlayEvent;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

@ClientFeature(
    mod = CharmClient.MOD_ID,
    description = "Playing a music disc in a jukebox prevents background music from playing at the same time."
)
public class DiscsStopBackgroundMusic extends CharmFeature {
    private static final int CHECK_TICKS = 10;
    private SoundInstance musicToStop = null;
    private int ticksBeforeStop = 0;

    @Override
    public void register() {
        BlockUseEvent.INSTANCE.handle(this::handleBlockUse);
        SoundPlayEvent.INSTANCE.handle(this::handleSoundPlay);
        ClientTickEvent.INSTANCE.handle(this::handleClientTick);
    }

    private void handleClientTick(Minecraft minecraft) {
        checkActuallyStopMusic();
    }

    private void handleSoundPlay(SoundEngine soundEngine, SoundInstance soundInstance) {
        checkShouldStopMusic(soundInstance);
    }

    private InteractionResult handleBlockUse(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        stopRecord(player, hitResult.getBlockPos(), player.getItemInHand(hand));
        return InteractionResult.PASS;
    }

    public void stopRecord(Entity entity, BlockPos pos, ItemStack stack) {
        if (entity.level().isClientSide
            && entity instanceof Player
            && stack.getItem() instanceof RecordItem
        ) {
            var state = entity.level().getBlockState(pos);
            if (state.getBlock() == Blocks.JUKEBOX && !state.getValue(JukeboxBlock.HAS_RECORD)) {
                getSoundManager().stop(null, SoundSource.MUSIC);
            }
        }
    }

    public void checkShouldStopMusic(SoundInstance sound) {
        if (sound.getSource() == SoundSource.MUSIC) {
            // check if there are any records playing
            getPlayingSounds().forEach((category, s) -> {
                if (category == SoundSource.RECORDS) {
                    musicToStop = sound;
                    CharmClient.instance().log().debug(getClass(), "Triggered background music while music disc playing");
                }
            });
        }
    }

    public void checkActuallyStopMusic() {
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
        var soundEngine = ((SoundManagerAccessor)getSoundManager()).getSoundEngine();
        return ((SoundEngineAccessor)soundEngine).getInstanceBySource();
    }
}
