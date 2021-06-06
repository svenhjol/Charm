package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

public interface PlaySoundCallback {
    Event<PlaySoundCallback> EVENT = EventFactory.createArrayBacked(PlaySoundCallback.class, (listeners) -> (soundSystem, sound) -> {
        for (PlaySoundCallback listener : listeners) {
            listener.interact(soundSystem, sound);
        }
    });

    void interact(SoundEngine soundSystem, SoundInstance sound);
}
