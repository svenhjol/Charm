package svenhjol.charm.client;

import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.world.ClientWorld;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ClientHandler;
import svenhjol.charm.module.SnowStorms;

public class SnowStormsClient extends CharmClientModule {
    public SnowStormsClient(CharmModule module) {
        super(module);
    }

    public static boolean tryHeavySnowTexture(ClientWorld world, TextureManager textureManager, float gradient) {
        float h = world.getThunderGradient(gradient);
        if (h > 0.0F && ClientHandler.enabled(SnowStormsClient.class) && SnowStorms.heavierSnowTexture) {
            textureManager.bindTexture(SnowStorms.HEAVY_SNOW);
            return true;
        }

        return false;
    }
}
