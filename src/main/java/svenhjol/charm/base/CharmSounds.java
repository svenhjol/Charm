package svenhjol.charm.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;

public class CharmSounds
{
    public static final SoundEvent BOOKSHELF_OPEN = createSound("bookshelf_open");
    public static final SoundEvent BOOKSHELF_CLOSE = createSound("bookshelf_close");
    public static final SoundEvent HOMING = createSound("homing");
    public static final SoundEvent WOOD_SMASH = createSound("wood_smash");
    public static final SoundEvent FUMEROLE_BUBBLING = createSound("fumerole_bubbling");
    public static final SoundEvent FUMEROLE_ERUPT = createSound("fumerole_erupt");

    public static SoundEvent createSound(String name)
    {
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, name);
        return new SoundEvent(res).setRegistryName(res);
    }
}
