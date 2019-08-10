package svenhjol.charm.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.handler.RegistryHandler;

public class CharmSounds
{
    public static final SoundEvent WOOD_SMASH = createSound("wood_smash");

    public static SoundEvent createSound(String name)
    {
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, name);
        SoundEvent sound = new SoundEvent(res).setRegistryName(res);
        RegistryHandler.SOUNDS.add(sound);
        return sound;
    }

    public static void init() {}
}
