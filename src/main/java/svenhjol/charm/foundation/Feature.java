package svenhjol.charm.foundation;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.foundation.feature.ConditionalRunner;

import java.util.List;
import java.util.function.BooleanSupplier;

public abstract class Feature implements ConditionalRunner {
    protected Loader<? extends Feature> loader;
    protected Log log;
    private boolean enabled = true;
    private boolean enabledInConfig = true;

    public Feature(Loader<? extends Feature> loader) {
        this.loader = loader;
        this.log = new Log(loader.id(), name());
        this.log.dev("I'm alive");
    }

    public Log log() {
        return log;
    }

    public String description() {
        return loader().metadata(this)
            .map(m -> m.description)
            .orElse("");
    }

    public int priority() {
        return loader().metadata(this)
            .map(m -> m.priority)
            .orElse(0);
    }

    public boolean canBeDisabled() {
        return loader().metadata(this)
            .map(m -> m.canBeDisabled)
            .orElse(true);
    }

    public boolean isEnabledByDefault() {
        return loader().metadata(this)
            .map(m -> m.enabledByDefault)
            .orElse(true);
    }

    public abstract Loader<? extends Feature> loader();

    public abstract Registry registry();

    public ResourceLocation id(String id) {
        return loader.id(id);
    }

    public String modId() {
        return loader.id();
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean isEnabled() {
        return enabled && enabledInConfig;
    }

    public boolean isEnabledInConfig() {
        return enabledInConfig;
    }

    public List<BooleanSupplier> checks() {
        return List.of();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setEnabledInConfig(boolean enabledInConfig) {
        this.enabledInConfig = enabledInConfig;
    }
}
