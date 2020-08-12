package svenhjol.charm.crafting.compat;

import net.minecraft.block.SoundType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class FutureMcSounds
{
    public static SoundType getLanternSoundType()
    {
        SoundEvent lanternPlace = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("futuremc", "lantern_place"));
        SoundEvent lanternBreak = ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("futuremc", "lantern_break"));
        
        if (lanternPlace == null || lanternBreak == null) return null;
        else return new SoundType(1.0f, 1.0f, lanternBreak, lanternPlace, lanternPlace, lanternBreak, lanternPlace);
    }
}
