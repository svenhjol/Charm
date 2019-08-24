package svenhjol.meson.helper;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SoundHelper
{
    @OnlyIn(Dist.CLIENT)
    public static void playSoundAtPos(BlockPos pos, SoundEvent sound, float volume, float pitch)
    {
        playSoundAtPos(pos, sound, SoundCategory.AMBIENT, volume, pitch);
    }

    @OnlyIn(Dist.CLIENT)
    public static void playSoundAtPos(BlockPos pos, SoundEvent sound, SoundCategory category, float volume, float pitch)
    {
        ClientWorld world = ClientHelper.getClientWorld();
        world.playSound(pos.getX(), pos.getY(), pos.getZ(), sound, category, volume, pitch, true);
    }

    @OnlyIn(Dist.CLIENT)
    public static void playSoundAtPos(PlayerEntity player, SoundEvent sound, SoundCategory category, float volume, float pitch)
    {
        playSoundAtPos(player.getPosition(), sound, category, volume, pitch);
    }

    public static SoundHandler getSoundHandler()
    {
        return Minecraft.getInstance().getSoundHandler();
    }

    @OnlyIn(Dist.CLIENT)
    public static Multimap<SoundCategory, ISound> getPlayingSounds()
    {
        Multimap<SoundCategory, ISound> sounds = getSoundHandler().sndManager.field_217943_n;
        return sounds;
    }
}
