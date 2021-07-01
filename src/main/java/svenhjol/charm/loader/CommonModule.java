package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public abstract class CommonModule implements ICharmModule {
    private String modId = "";
    private String description = "";
    private int priority = 0;
    private boolean enabled = true;
    private boolean enabledByDefault = true;
    private boolean alwaysEnabled = false;

    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public void setModId(String modId) {
        this.modId = modId;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    public void setEnabledByDefault(boolean flag) {
        this.enabledByDefault = flag;
    }

    public boolean isAlwaysEnabled() {
        return alwaysEnabled;
    }

    public void setAlwaysEnabled(boolean flag) {
        this.alwaysEnabled = flag;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Provide a list of recipe IDs to remove.
     * This allows a module to conditionally remove recipes according to its config.
     * @return Recipe IDs to remove.
     */
    public List<ResourceLocation> getRecipesToRemove() {
        return new ArrayList<>();
    }

    // TODO
    public List<ResourceLocation> getAdvancementsToRemove() {
        return new ArrayList<>();
    }
}
