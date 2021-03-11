package svenhjol.charm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.world.ClientWorld;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.handler.ClientHandler;
import svenhjol.charm.module.SnowStorms;

public class SnowStormsClient extends CharmClientModule {
    public SnowStormsClient(CharmModule module) {
        super(module);
    }

    public static boolean tryHeavySnowTexture(ClientWorld world, float gradient) {
        float h = world.getThunderGradient(gradient);
        if (h > 0.0F && ClientHandler.enabled(SnowStormsClient.class) && SnowStorms.heavierSnowTexture) {
            RenderSystem.setShaderTexture(0, SnowStorms.HEAVY_SNOW);
            return true;
        }

        return false;
    }
}
