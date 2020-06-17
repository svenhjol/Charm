package svenhjol.charm.decoration.client;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import svenhjol.charm.decoration.module.GoldLanterns;

public class GoldLanternsClient {
    public GoldLanternsClient() {
        RenderType transparentRenderType = RenderType.getCutoutMipped();
        RenderTypeLookup.setRenderLayer(GoldLanterns.block, transparentRenderType);
    }
}
