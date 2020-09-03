package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.block.BrewBottleBlock;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "")
public class Innkeepers extends MesonModule {
    public static BrewBottleBlock BREW_BOTTLE;

    @Override
    public void init() {
        BREW_BOTTLE = new BrewBottleBlock(this);
    }

    @Override
    public void initClient() {
        RenderLayersAccessor.getBlocks().put(BREW_BOTTLE, RenderLayer.getCutout());
    }
}
