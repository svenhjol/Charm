package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;

public interface PlaySoundEvent {
    Event<PlaySoundEvent> EVENT = EventFactory.createArrayBacked(PlaySoundEvent.class, (listeners) -> (soundSystem, sound) -> {
        for (PlaySoundEvent listener : listeners) {
            listener.interact(soundSystem, sound);
        }
    });

    void interact(SoundEngine soundSystem, SoundInstance sound);
}
