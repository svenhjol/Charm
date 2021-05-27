package svenhjol.charm.module.snow_storms;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.world.ClientWorld;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ClientHandler;

public class SnowStormsClient extends CharmClientModule {
    public SnowStormsClient(CharmModule module) {
        super(module);
    }

    public static void tryHeavySnowTexture(ClientWorld world, float gradient) {
        float h = world.getThunderGradient(gradient);
        if (h > 0.0F && ClientHandler.enabled(SnowStormsClient.class) && SnowStorms.heavierSnowTexture) {
            RenderSystem.setShaderTexture(0, SnowStorms.HEAVY_SNOW);
        }
    }
}
