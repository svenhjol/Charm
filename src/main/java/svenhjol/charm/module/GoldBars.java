package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.Charm;
import svenhjol.charm.block.GoldBarsBlock;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

@Module(mod = Charm.MOD_ID, description = "Gold variant of vanilla iron bars.")
public class GoldBars extends CharmModule {
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
