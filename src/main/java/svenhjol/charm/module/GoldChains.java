package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.block.GoldChainBlock;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Gold version of the vanilla chain.")
public class GoldChains extends MesonModule {
    public static GoldChainBlock GOLD_CHAIN;

    @Override
    public void init() {
        GOLD_CHAIN = new GoldChainBlock(this);
    }

    @Override
    public void setupClient() {
        RenderLayersAccessor.getBlocks().put(GOLD_CHAIN, RenderLayer.getCutout());
    }
}
