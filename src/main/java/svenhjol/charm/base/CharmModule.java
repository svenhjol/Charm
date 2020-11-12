package svenhjol.charm.base;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public abstract class CharmModule {
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean alwaysEnabled = false;
    public String description = "";
    public String mod = "";

    public boolean depends() {
        return true;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Nullable
    public Class<? extends CharmClientModule> getClientClass() {
        return null;
    }

    public Class<? extends CharmServerModule> getServerClass() { return null; }

    public List<Identifier> getRecipesToRemove() {
        return new ArrayList<>();
    }

    public void register() {
        // run on both sides, even if module disabled
    }

    public void init() {
        // run on both sides, only if module enabled
    }

    public void loadWorld(MinecraftServer server) {
        // run on integrated server, only if module enabled
    }
}
