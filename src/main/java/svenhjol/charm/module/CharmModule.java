package svenhjol.charm.module;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.helper.StringHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class CharmModule {
    public boolean enabled = true;
    public boolean enabledByDefault = true;
    public boolean alwaysEnabled = false;
    public String description = "";
    public String mod = "";
    public int priority = 0;
    public List<String> requiresMixins = new ArrayList<>();
    public Class<? extends CharmClientModule> client = null;

    public boolean depends() {
        return true;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public ResourceLocation getId() {
        return new ResourceLocation(this.mod, StringHelper.upperCamelToSnake(getName()));
    }

    /**
     * Provide a list of recipe IDs to remove.
     * This allows a module to conditionally remove recipes according to its config.
     * @return Recipe IDs to remove.
     */
    public List<ResourceLocation> getRecipesToRemove() {
        return new ArrayList<>();
    }

    public void register() {
        // run on both sides, even if module disabled (this.enabled is available)
    }

    public void init() {
        // run on both sides, only if module enabled
    }
}
