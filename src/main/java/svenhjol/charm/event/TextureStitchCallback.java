package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;

import java.util.Set;

public interface TextureStitchCallback {
    Event<TextureStitchCallback> EVENT = EventFactory.createArrayBacked(TextureStitchCallback.class, (listeners) -> (atlas, textures) -> {
        for (TextureStitchCallback listener : listeners) {
            listener.interact(atlas, textures);
        }
    });

    void interact(SpriteAtlasTexture atlas, Set<Identifier> textures);
}
