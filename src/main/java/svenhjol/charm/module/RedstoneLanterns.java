package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.block.RedstoneLanternBlock;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "A lantern that emits light when a redstone signal is received.")
public class RedstoneLanterns extends MesonModule {
    public static RedstoneLanternBlock REDSTONE_LANTERN;

    @Override
    public void init() {
        REDSTONE_LANTERN = new RedstoneLanternBlock(this);
    }

    @Override
    public void setupClient() {
        RenderLayersAccessor.getBlocks().put(REDSTONE_LANTERN, RenderLayer.getCutout());
    }
}
