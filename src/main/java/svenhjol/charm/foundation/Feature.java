package svenhjol.charm.foundation;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.foundation.feature.Conditional;

import java.util.List;
import java.util.function.BooleanSupplier;

public abstract class Feature implements Conditional {
    protected final Loader<? extends Feature> loader;
    protected Log log;
    private boolean enabled = true;
    private boolean enabledInConfig = true;

    public Feature(Loader<? extends Feature> loader) {
        this.loader = loader;
        this.log = new Log(loader.id(), name());
        this.log.dev(name() + " is alive");
    }

    public final String description() {
        return loader().metadata(this)
            .map(m -> m.description)
            .orElse("");
    }

    public final int priority() {
        return loader().metadata(this)
            .map(m -> m.priority)
            .orElse(0);
    }

    public final boolean isEnabledByDefault() {
        return loader().metadata(this)
            .map(m -> m.enabledByDefault)
            .orElse(true);
    }

    public boolean canBeDisabled() {
        return true;
    }

    public abstract Loader<? extends Feature> loader();

    public abstract Registry registry();

    public Log log() {
        return log;
    }

    public ResourceLocation id(String id) {
        return loader.id(id);
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
