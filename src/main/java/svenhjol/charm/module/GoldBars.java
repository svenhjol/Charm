package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.block.GoldBarsBlock;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Gold variant of vanilla iron bars.")
public class GoldBars extends MesonModule {
    public static GoldBarsBlock GOLD_BARS;

    @Override
    public void init() {
        GOLD_BARS = new GoldBarsBlock(this);
    }

    @Override
    public void afterInitClient() {
        RenderLayersAccessor.getBlocks().put(GOLD_BARS, RenderLayer.getCutout());
    }
}
