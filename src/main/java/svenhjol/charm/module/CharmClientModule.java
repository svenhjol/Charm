package svenhjol.charm.module;

public abstract class CharmClientModule {
    protected CharmModule module;

    public CharmClientModule(CharmModule module) {
        this.module = module;
    }

    public CharmModule getModule() {
        return module;
    }

    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void register() {
        // run on client side, even if module disabled
    }

    public void init() {
        // run on client side, only if module enabled
    }
}
