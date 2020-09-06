package svenhjol.meson;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public abstract class MesonModule {
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean alwaysEnabled = false;
    public String description = "";
    public MesonMod mod;

    public boolean depends() {
        return true;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public List<Identifier> getRecipesToRemove() {
        return new ArrayList<>();
    }

    public void init() {
        // run on both sides, even if not enabled (enabled flag is available)
    }

    public void initClient() {
        // run on client, even if not enabled (enabled flag is available)
    }

    public void afterInit() {
        // run on both sides, only executed if module enabled
    }

    public void afterInitClient() {
        // run on both sides, only executed if module enabled
    }

    public void setup() {
        // run on both sides on world load, only executed if module enabled
    }

    public void setupClient() {
        // run on client on world load, only executed if module enabled
    }
}
