package svenhjol.charm.charmony.callback;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

/**
 * A custom Fabric event that fires whenever a sound plays.
 */
public interface PlaySoundCallback {
    Event<PlaySoundCallback> EVENT = EventFactory.createArrayBacked(PlaySoundCallback.class, (listeners) -> (soundEngine, sound) -> {
        for (PlaySoundCallback listener : listeners) {
            listener.interact(soundEngine, sound);
        }
    });

    void interact(SoundEngine soundEngine, SoundInstance sound);
}
