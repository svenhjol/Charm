package svenhjol.charm.module;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.GoldBarsBlock;

@Module(mod = Charm.MOD_ID, description = "Gold variant of vanilla iron bars.")
public class GoldBars extends CharmModule {
    public static GoldBarsBlock GOLD_BARS;

    @Override
    public void register() {
        GOLD_BARS = new GoldBarsBlock(this);
    }

    @Override
    public void clientInit() {
        BlockRenderLayerMap.INSTANCE.putBlock(GOLD_BARS, RenderLayer.getCutout());
    }
}
