package svenhjol.charm.loader;

public abstract class ClientModule implements ICharmModule {
    private CommonModule parentModule;
    private boolean enabled = true;
    private int priority = 0;

    @Override
    public String getModId() {
        return getParentModule().getModId();
    }

    @Override
    public void setModId(String modId) {
        // no op
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

    public CommonModule getParentModule() {
        return parentModule;
    }

    public void setParentModule(CommonModule module) {
        this.parentModule = module;
    }
}
