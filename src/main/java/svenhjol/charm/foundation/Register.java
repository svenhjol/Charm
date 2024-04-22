package svenhjol.charm.foundation;

public abstract class Register<T extends Feature> {
    protected T feature;

    public Register(T feature) {
        this.feature = feature;
    }

    public int priority() {
        return 0;
    }

    public abstract void onRegister();
}
