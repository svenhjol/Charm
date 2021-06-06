package svenhjol.charm.module;

import svenhjol.charm.module.CharmModule;

public abstract class CharmClientModule {
    protected svenhjol.charm.module.CharmModule module;

    public CharmClientModule(svenhjol.charm.module.CharmModule module) {
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
