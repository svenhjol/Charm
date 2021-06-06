package svenhjol.charm.module.snow_storms;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.multiplayer.ClientLevel;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.handler.ClientHandler;
import svenhjol.charm.module.snow_storms.SnowStorms;

public class SnowStormsClient extends CharmClientModule {
    public SnowStormsClient(CharmModule module) {
        super(module);
    }

    public static void tryHeavySnowTexture(ClientLevel world, float gradient) {
        float h = world.getThunderLevel(gradient);
        if (h > 0.0F && ClientHandler.enabled(SnowStormsClient.class) && svenhjol.charm.module.snow_storms.SnowStorms.heavierSnowTexture) {
            RenderSystem.setShaderTexture(0, SnowStorms.HEAVY_SNOW);
        }
    }
}
