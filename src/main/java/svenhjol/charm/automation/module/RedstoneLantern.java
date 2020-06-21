package svenhjol.charm.automation.module;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.automation.block.RedstoneLanternBlock;
import svenhjol.charm.base.CharmCategories;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.AUTOMATION,
    description = "A lantern that emits light when a redstone signal is received.")
public class RedstoneLantern extends MesonModule {
    public static RedstoneLanternBlock block;

    @Override
    public void init() {
        block = new RedstoneLanternBlock(this);
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(block, RenderType.getCutout());
    }
}
