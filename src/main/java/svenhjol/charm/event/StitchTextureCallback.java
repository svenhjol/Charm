package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;

public interface StitchTextureCallback {
    Event<StitchTextureCallback> EVENT = EventFactory.createArrayBacked(StitchTextureCallback.class, listeners -> (atlas, textures) -> {
        for (StitchTextureCallback listener : listeners) {
            listener.interact(atlas, textures);
        }
    });

    void interact(TextureAtlas atlas, Set<ResourceLocation> textures);
}
