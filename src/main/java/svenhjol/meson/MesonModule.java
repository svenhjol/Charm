package svenhjol.meson;

public abstract class MesonModule {
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean alwaysEnabled = false;
    public Meson mod;

    public boolean shouldSetup() {
        return true;
    }

    public boolean depends() {
        return true;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void init() {
        // `enabled` available at this stage.
    }
}
