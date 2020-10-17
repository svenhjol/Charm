package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundSystem;

public interface PlaySoundCallback {
    Event<PlaySoundCallback> EVENT = EventFactory.createArrayBacked(PlaySoundCallback.class, (listeners) -> (soundSystem, sound) -> {
        for (PlaySoundCallback listener : listeners) {
            listener.interact(soundSystem, sound);
        }
    });

    void interact(SoundSystem soundSystem, SoundInstance sound);
}
