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

    public void register() {
        // run on both sides, even if not enabled (enabled flag is available)
    }

    public void registerClient() {
        // run on client, even if not enabled (enabled flag is available)
    }

    public void initWhenEnabled() {
        // run on both sides, only executed if module enabled
    }

    public void initClientWhenEnabled() {
        // run on both sides, only executed if module enabled
    }

    public void initWorldWhenEnabled() {
        // run on both sides on world load, only executed if module enabled
    }

    public void initClientWorldWhenEnabled() {
        // run on client on world load, only executed if module enabled
    }
}
