package svenhjol.charm.tweaks.feature;

import net.minecraft.block.BlockJukebox;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.audio.ISound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import svenhjol.meson.Feature;
import svenhjol.meson.Meson;
import svenhjol.meson.helper.SoundHelper;

public class RecordsStopBackgroundMusic extends Feature
{
    public static int stopAfterTicks; // number of ticks before music stops
    private int ticks; // delay
    private ISound currentMusic = null; // the music that plays while record is playing

    @Override
    public String getDescription()
    {
        return "Stops the background music when playing a record in a jukebox to avoid a horrible cacophony.";
    }

    @Override
    public void setupConfig()
    {
        // internal
        stopAfterTicks = 10;
    }

    /**
     * Stop music when activating a jukebox with any record.
     */
    @SubscribeEvent
    public void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
    {
        if (event.getWorld().isRemote) {
            IBlockState state = event.getWorld().getBlockState(event.getPos());

            if (event.getEntity() instanceof EntityPlayer
                    && event.getItemStack().getItem() instanceof ItemRecord
                    && state.getBlock() == Blocks.JUKEBOX
                    && !state.getValue(BlockJukebox.HAS_RECORD)
            ) {
                SoundHelper.getPlayingSounds().forEach((id, sound) ->
                {
                    if (sound.getCategory() == SoundCategory.MUSIC) {
                        Meson.debug("Music playing before record started");
                        currentMusic = sound; // schedule the music to be stopped
                    }
                });
            }
        }
    }

    /**
     * Stop music if it starts again while record is playing.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onSoundPlay(SoundEvent.SoundSourceEvent event)
    {
        ISound triggeredSound = event.getSound();

        if (triggeredSound.getCategory() == SoundCategory.MUSIC) {
            SoundHelper.getPlayingSounds().forEach((id, sound) -> {
                if (sound.getCategory() == SoundCategory.RECORDS) {
                    Meson.debug("Music tried to start while record playing");
                    currentMusic = triggeredSound;
                }
            });
        }
    }

    /**
     * Trigger music stop if there is music playing.
     */
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onTick(TickEvent.ClientTickEvent event)
    {
        if (event.phase == TickEvent.Phase.END && currentMusic != null) {
            if (++ticks % stopAfterTicks > 0) return;
            Meson.debug("Stopping the music");
            SoundHelper.getSoundManager().stopSound(currentMusic);
            ticks = 0;
            currentMusic = null;
        }
    }

    @Override
    public boolean hasSubscriptions()
    {
        return isClient();
    }
}
