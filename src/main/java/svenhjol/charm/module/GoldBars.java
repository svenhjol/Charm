package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.block.GoldBarsBlock;
import svenhjol.meson.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Gold variant of vanilla iron bars.")
public class GoldBars extends MesonModule {
    public static GoldBarsBlock GOLD_BARS;

    @Override
    public void register() {
        GOLD_BARS = new GoldBarsBlock(this);
    }

    @Override
    public void clientRegister() {
        RenderLayersAccessor.getBlocks().put(GOLD_BARS, RenderLayer.getCutout());
    }
}
