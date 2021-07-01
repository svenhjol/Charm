package svenhjol.charm.loader;

public abstract class ServerModule implements ICharmModule {
    private String modId = "";
    private boolean enabled = true;
    private int priority = 0;

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
    public int getPriority() {
        return priority;
    }

    @Override
    public void setEnabled(boolean flag) {
        this.enabled = flag;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
