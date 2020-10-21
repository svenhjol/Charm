package svenhjol.charm.module;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.GoldLanternBlock;

@Module(mod = Charm.MOD_ID, description = "Gold version of the vanilla lanterns.")
public class GoldLanterns extends CharmModule {
    public static GoldLanternBlock GOLD_LANTERN;
    public static GoldLanternBlock GOLD_SOUL_LANTERN;

    @Override
    public void register() {
        GOLD_LANTERN = new GoldLanternBlock(this, "gold_lantern");
        GOLD_SOUL_LANTERN = new GoldLanternBlock(this, "gold_soul_lantern");
    }

    @Override
    public void clientRegister() {
        BlockRenderLayerMap.INSTANCE.putBlock(GOLD_LANTERN, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(GOLD_SOUL_LANTERN, RenderLayer.getCutout());
    }
}
