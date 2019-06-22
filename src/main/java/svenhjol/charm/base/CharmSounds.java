package svenhjol.charm.base;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import svenhjol.charm.Charm;
import svenhjol.meson.registry.ProxyRegistry;

public class CharmSounds
{
    public static final SoundEvent WOOD_OPEN = createSoundEvent("wood_open");
    public static final SoundEvent WOOD_CLOSE = createSoundEvent("wood_close");
    public static final SoundEvent WOOD_SMASH = createSoundEvent("wood_smash");
    public static final SoundEvent BOOKSHELF_OPEN = createSoundEvent("bookshelf_open");
    public static final SoundEvent BOOKSHELF_CLOSE = createSoundEvent("bookshelf_close");
    public static final SoundEvent LITTLE_FIRE = createSoundEvent("little_fire");
    public static final SoundEvent SPECTRE_AMBIENT = createSoundEvent("spectre_ambient");
    public static final SoundEvent SPECTRE_DEATH = createSoundEvent("spectre_disappear");
    public static final SoundEvent SPECTRE_HIT = createSoundEvent("spectre_hit");
    public static final SoundEvent SPECTRE_MOVE = createSoundEvent("spectre_move");
    public static final SoundEvent HOMING = createSoundEvent("homing");
    public static final SoundEvent ENDER_WHISPERS = createSoundEvent("whispers");
    public static final SoundEvent ENDER_ABOVE_STRONGHOLD = createSoundEvent("above_stronghold");
    public static final SoundEvent ENDER_RESONANCE = createSoundEvent("ender_resonance");

    public static SoundEvent createSoundEvent(String name)
    {
        ResourceLocation res = new ResourceLocation(Charm.MOD_ID, name);
        return new SoundEvent(res).setRegistryName(res);
    }

    public static void registerSounds()
    {
        ProxyRegistry.registerAll(
            WOOD_OPEN,
            WOOD_CLOSE,
            WOOD_SMASH,
            BOOKSHELF_OPEN,
            BOOKSHELF_CLOSE,
            LITTLE_FIRE,
            SPECTRE_AMBIENT,
            SPECTRE_DEATH,
            SPECTRE_HIT,
            SPECTRE_MOVE,
            HOMING,
            ENDER_WHISPERS,
            ENDER_ABOVE_STRONGHOLD,
            ENDER_RESONANCE
        );
    }
}
