package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.Charm;
import svenhjol.charm.block.GoldLanternBlock;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;

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
        RenderLayersAccessor.getBlocks().put(GOLD_LANTERN, RenderLayer.getCutout());
        RenderLayersAccessor.getBlocks().put(GOLD_SOUL_LANTERN, RenderLayer.getCutout());
    }
}
