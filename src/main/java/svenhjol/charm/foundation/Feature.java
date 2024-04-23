package svenhjol.charm.foundation;

import java.util.List;
import java.util.function.BooleanSupplier;

public abstract class Feature {
    protected Loader<? extends Feature> loader;
    protected Log log;
    private boolean enabled = true;
    private boolean enabledInConfig = true;

    public void onInit(Loader<? extends Feature> loader) {
        this.loader = loader;
        this.log = new Log(name());
    }

    public abstract Loader<? extends Feature> loader();

    public abstract Registry registry();

    public List<? extends Register<? extends Feature>> register() {
        return List.of();
    }

    public List<? extends Network<? extends Feature>> network() {
        return List.of();
    }

    public void onEnabled() {
        // no op
    }

    public void onDisabled() {
        // no op
    }

    public String modId() {
        return loader.id();
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    public boolean isEnabled() {
        return enabled && enabledInConfig;
    }

    public boolean canBeDisabled() {
        return true;
    }

    public boolean isEnabledByDefault() {
        return true;
    }

    public boolean isEnabledInConfig() {
        return enabledInConfig;
    }

    public int priority() {
        return 0;
    }

    public String description() {
        return "";
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
