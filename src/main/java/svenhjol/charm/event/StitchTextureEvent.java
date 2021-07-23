package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import java.util.Set;

public interface StitchTextureEvent {
    Event<StitchTextureEvent> EVENT = EventFactory.createArrayBacked(StitchTextureEvent.class, (listeners) -> (atlas, textures) -> {
        for (StitchTextureEvent listener : listeners) {
            listener.interact(atlas, textures);
        }
    });

    void interact(TextureAtlas atlas, Set<ResourceLocation> textures);
}
