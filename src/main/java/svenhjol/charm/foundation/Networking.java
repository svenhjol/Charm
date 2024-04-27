package svenhjol.charm.foundation;

public abstract class Networking<T extends Feature> {
    protected T feature;

    public Networking(T feature) {
        this.feature = feature;
    }

    public int priority() {
        return 0;
    }

    public void onRegister() {
        // no op
    }

    public void onEnabled() {
        // no op
    }

    public void onDisabled() {
        // no op
    }
}
