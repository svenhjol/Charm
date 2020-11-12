package svenhjol.charm.base;

import net.minecraft.client.MinecraftClient;

public abstract class CharmClientModule {
    private CharmModule module;

    public CharmClientModule(CharmModule module) {
        this.module = module;
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

    public void loadWorld(MinecraftClient client) {
        // run on client side, only if module enabled
    }
}
