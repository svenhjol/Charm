package svenhjol.charm.charmony.feature;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.event.LevelLoadEvent;

public abstract class RegisterHolder<F extends Feature> extends FeatureHolder<F> implements Conditional {
    public RegisterHolder(F feature) {
        super(feature);
        feature.loader().registerConditional(this);

        // Call onWorldLoaded once when the world has finished loading.
        LevelLoadEvent.INSTANCE.handle((server, level) -> {
            if (level.dimension() == ServerLevel.OVERWORLD) {
                onWorldLoaded(server, level);
            }
        });
    }

    /**
     * Convenience method to register things that require tags.
     * Only runs once per world load. If you need to run something per dimension, use LevelLoadEvent.
     */
    public void onWorldLoaded(MinecraftServer server, ServerLevel level) {
        // no op
    }

    @Override
    public boolean isEnabled() {
        return feature().isEnabled();
    }
}
