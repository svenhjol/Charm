package svenhjol.charm.helper;

import com.google.common.collect.Multimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;

/**
 * @version 1.0.0-charm
 */
public class SoundHelper {
    public static SoundManager getSoundManager() {
        return Minecraft.getInstance().getSoundManager();
    }

    public static Multimap<SoundSource, SoundInstance> getPlayingSounds() {
        SoundEngine soundSystem = getSoundManager().soundEngine;
        return soundSystem.instanceBySource;
    }
}