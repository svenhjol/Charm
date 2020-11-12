package svenhjol.charm.base;

import net.minecraft.client.MinecraftClient;

public abstract class CharmClient {
    public String getName() {
        return this.getClass().getSimpleName();
    }

    public void register() {
    }

    public void init() {
    }

    public void loadWorld(MinecraftClient client) {
    }
}
