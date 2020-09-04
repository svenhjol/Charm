package svenhjol.charm.module;

import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.block.WoodcutterBlock;
import svenhjol.charm.mixin.accessor.RenderLayersAccessor;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(description = "")
public class Lumberjacks extends MesonModule {
    public static WoodcutterBlock WOODCUTTER;

    @Override
    public void init() {
        WOODCUTTER = new WoodcutterBlock(this);
    }

    @Override
    public void initClient() {
        RenderLayersAccessor.getBlocks().put(WOODCUTTER, RenderLayer.getCutout());
    }
}
