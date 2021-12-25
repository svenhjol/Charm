package svenhjol.charm.module.core;

import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = Core.class)
public class CoreClient extends CharmModule {
    public final InventoryButtonManager inventoryButtonManager;

    public CoreClient() {
        this.inventoryButtonManager = new InventoryButtonManager();
    }

    @Override
    public void register() {
        // call the register method of inventoryButtonManager
        this.inventoryButtonManager.register();
    }

    @Override
    public void runWhenEnabled() {
        // proxy calls
        this.inventoryButtonManager.runWhenEnabled();
    }
}
