package svenhjol.charm.foundation;

public abstract class Register<T extends Feature> {
    protected T feature;
    protected Log log;

    public Register(T feature) {
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
