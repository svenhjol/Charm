package svenhjol.charm.module;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.block.RedstoneLanternBlock;

@Module(mod = Charm.MOD_ID, description = "A lantern that emits light when a redstone signal is received.")
public class RedstoneLanterns extends CharmModule {
    public static RedstoneLanternBlock REDSTONE_LANTERN;

    @Override
    public void register() {
        REDSTONE_LANTERN = new RedstoneLanternBlock(this);
    }

    @Override
    public void clientRegister() {
        BlockRenderLayerMap.INSTANCE.putBlock(REDSTONE_LANTERN, RenderLayer.getCutout());
    }
}
