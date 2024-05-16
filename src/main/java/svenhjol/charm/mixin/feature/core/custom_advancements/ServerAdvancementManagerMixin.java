package svenhjol.charm.mixin.feature.core.custom_advancements;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import svenhjol.charm.feature.core.custom_advancements.CustomAdvancements;
import svenhjol.charm.foundation.Resolve;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerAdvancementManager.class)
public abstract class ServerAdvancementManagerMixin extends SimpleJsonResourceReloadListener {
    public ServerAdvancementManagerMixin(Gson gson, String string) {
        super(gson, string);
    }

    @Override
    protected Map<ResourceLocation, JsonElement> prepare(ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        var handlers = Resolve.feature(CustomAdvancements.class).handlers;
        var map = super.prepare(resourceManager, profilerFiller);
        var copy = new HashMap<>(map);
        map.forEach((key, val) -> {
            if (handlers.shouldRemove(key)) {
                copy.remove(key);
            }
        });
        return copy;
    }
}
