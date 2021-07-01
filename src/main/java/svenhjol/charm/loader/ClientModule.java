package svenhjol.charm.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public abstract class ClientModule implements ICharmModule {
    private CommonModule parentModule;
    private int priority = 0;
    private final List<Predicate<ICharmModule>> dependencies = new ArrayList<>();

    @Override
    public String getModId() {
        return getParentModule().getModId();
    }

    @Override
    public void setModId(String modId) {
        // no op
    }

    @Override
    public List<Predicate<ICharmModule>> getDependencies() {
        return dependencies;
    }

    @Override
    public void addDependencyCheck(Predicate<ICharmModule> test) {
        dependencies.add(test);
    }

    @Override
    public boolean isEnabled() {
        return getParentModule().isEnabled();
    }

    @Override
    public void setEnabled(boolean flag) {
        // no op
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public CommonModule getParentModule() {
        return parentModule;
    }

    public void setParentModule(CommonModule module) {
        this.parentModule = module;
    }
}
