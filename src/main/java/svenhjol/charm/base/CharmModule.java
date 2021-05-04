package svenhjol.charm.base;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import svenhjol.charm.base.helper.StringHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class CharmModule {
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean alwaysEnabled = false;
    public String description = "";
    public String mod = "";
    public int priority = 0;
    public Class<? extends CharmClientModule> client = null;

    public boolean depends() {
        return true;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public Identifier getId() {
        return new Identifier(this.mod, StringHelper.upperCamelToSnake(getName()));
    }

    /**
     * Provide a list of recipe IDs to remove.
     * This allows a module to conditionally remove recipes according to its config.
     * @return Recipe IDs to remove.
     */
    public List<Identifier> getRecipesToRemove() {
        return new ArrayList<>();
    }

    public void register() {
        // run on both sides, even if module disabled (this.enabled is available)
    }

    public void init() {
        // run on both sides, only if module enabled
    }

    public void loadWorld(MinecraftServer server) {
        // run on integrated server, only if module enabled
    }

    /**
     * Provide a list of required mixins.
     * If any are blacklisted, this module will be disabled.
     * Each string should be the class name not the fully qualified path.
     * @return List of required mixins.
     */
    public List<String> mixins() {
        return new ArrayList<>();
    }
}
