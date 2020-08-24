package svenhjol.meson;

public abstract class MesonModule {
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean alwaysEnabled = false;
    public MesonMod mod;

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
        // run on both sides, even if not enabled (enabled flag is available)
    }

    public void initClient() {
        // run on client, even if not enabled (enabled flag is available)
    }

    public void setup() {
        // run on both sides, only executed if module enabled
    }

    public void setupClient() {
        // run on client, only executed if module enabled
    }
}
