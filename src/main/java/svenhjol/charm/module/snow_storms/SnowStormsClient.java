package svenhjol.charm.module.snow_storms;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.multiplayer.ClientLevel;
import svenhjol.charm.CharmClient;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = SnowStorms.class)
public class SnowStormsClient extends CharmClientModule {

    public static void tryHeavySnowTexture(ClientLevel world, float gradient) {
        float h = world.getThunderLevel(gradient);
        if (h > 0.0F && CharmClient.LOADER.isEnabled(SnowStormsClient.class) && SnowStorms.heavierSnowTexture) {
            RenderSystem.setShaderTexture(0, SnowStorms.HEAVY_SNOW);
        }
    }
}
