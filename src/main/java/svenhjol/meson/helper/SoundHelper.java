package svenhjol.meson.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.util.Map;
import java.util.Random;

public class SoundHelper
{
    public static void playSoundAtPos(World world, BlockPos pos, SoundEvent sound, float volume, float pitch)
    {
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), sound, SoundCategory.AMBIENT, volume, pitch, true);
    }

    public static void playerSound(EntityPlayer player, SoundEvent sound, float volume, float pitch)
    {
        playerSound(player, sound, volume, pitch, SoundCategory.NEUTRAL);
    }

    public static void playerSound(EntityPlayer player, SoundEvent sound, float volume, float pitch, SoundCategory category)
    {
        if (category == null) {
            category = SoundCategory.NEUTRAL;
        }
        player.world.playSound(player, player.posX, player.posY, player.posZ, sound, category, volume, pitch);
    }

    public static void playerSound(EntityPlayer player, SoundEvent sound, float volume, float pitch, float pitchVariation, SoundCategory category)
    {
        pitch += (pitchVariation - (pitchVariation * new Random().nextFloat()) * 2);
        playerSound(player, sound, volume, pitch, category);
    }

    public static Map<String, ISound> getPlayingSounds()
    {
        return ReflectionHelper.getPrivateValue(SoundManager.class, getSoundManager(), ObfuscationHelper.Fields.PLAYING_SOUNDS);
    }

    public static SoundManager getSoundManager()
    {
        return ReflectionHelper.getPrivateValue(SoundHandler.class, Minecraft.getMinecraft().getSoundHandler(), ObfuscationHelper.Fields.SNDMANAGER);
    }
}
