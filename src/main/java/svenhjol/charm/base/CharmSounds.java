package svenhjol.charm.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;

public class CharmSounds
{
    public static final SoundEvent WOOD_SMASH = createSound("wood_smash");

    public static SoundEvent createSound(String name)
    {
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(res).setRegistryName(res);
        return sound;
    }

    public static void init() {}
}
