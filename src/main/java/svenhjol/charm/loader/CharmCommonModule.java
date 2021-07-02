package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class CharmCommonModule implements ICharmModule {
    private String modId = "";
    private String description = "";
    private int priority = 0;
    private boolean enabled = true;
    private boolean enabledInConfig = true;
    private boolean enabledByDefault = true;
    private boolean alwaysEnabled = false;
    private final List<Predicate<ICharmModule>> dependencies = new ArrayList<>();

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isEnabledInConfig() {
        return enabledInConfig;
    }

    @Override
    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    @Override
    public boolean isAlwaysEnabled() {
        return alwaysEnabled;
    }

    @Override
    public String getModId() {
        return modId;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public List<Predicate<ICharmModule>> getDependencies() {
        return dependencies;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setEnabledByDefault(boolean flag) {
        this.enabledByDefault = flag;
    }

    public void setAlwaysEnabled(boolean flag) {
        this.alwaysEnabled = flag;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public void setModId(String modId) {
        this.modId = modId;
    }

    @Override
    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    @Override
    public void setEnabledInConfig(boolean flag) {
        this.enabledInConfig = flag;
    }

    @Override
    public void addDependencyCheck(Predicate<ICharmModule> test) {
        dependencies.add(test);
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
