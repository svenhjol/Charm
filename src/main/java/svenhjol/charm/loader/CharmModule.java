package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.helper.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public abstract class CharmModule {
    private String modId = "";
    private String description = "";
    private int priority = 0;
    private boolean enabled = true;
    private boolean enabledInConfig = true;
    private boolean enabledByDefault = true;
    private boolean alwaysEnabled = false;
    private final List<Predicate<CharmModule>> dependencies = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isEnabledInConfig() {
        return enabledInConfig;
    }

    public boolean isEnabledByDefault() {
        return enabledByDefault;
    }

    public boolean isAlwaysEnabled() {
        return alwaysEnabled;
    }

    public String getModId() {
        return modId;
    }

    public String getDescription() {
        return description;
    }

    public List<Predicate<CharmModule>> getDependencies() {
        return dependencies;
    }

    public int getPriority() {
        return priority;
    }

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

    public void setModId(String modId) {
        this.modId = modId;
    }

    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    public void setEnabledInConfig(boolean flag) {
        this.enabledInConfig = flag;
    }

    public void addDependencyCheck(Predicate<CharmModule> test) {
        dependencies.add(test);
    }

    public ResourceLocation getId() {
        return new ResourceLocation(getModId(), StringHelper.upperCamelToSnake(getName()).toLowerCase(Locale.ROOT));
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void register() {
        // always run
    }

    public void runWhenEnabled() {
        // run if module is enabled
    }

    public void runWhenDisabled() {
        // run if module is disabled
    }
}
