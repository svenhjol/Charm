package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.block.GoldLanternBlock;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "Gold version of the vanilla lanterns.")
public class GoldLanterns extends MesonModule {
    public static GoldLanternBlock GOLD_LANTERN;
    public static GoldLanternBlock GOLD_SOUL_LANTERN;

    @Override
    public void init() {
        GOLD_LANTERN = new GoldLanternBlock(this, "gold_lantern");
        GOLD_SOUL_LANTERN = new GoldLanternBlock(this, "gold_soul_lantern");
    }

    @Override
    public void setupClient() {
        RenderLayersAccessor.getBlocks().put(GOLD_LANTERN, RenderLayer.getCutout());
        RenderLayersAccessor.getBlocks().put(GOLD_SOUL_LANTERN, RenderLayer.getCutout());
    }
}
