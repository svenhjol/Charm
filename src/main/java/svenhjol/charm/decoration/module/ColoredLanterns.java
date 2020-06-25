package svenhjol.charm.decoration.module;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.BaseLanternBlock;
import svenhjol.charm.decoration.block.ColoredLanternBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.enums.ColorVariant;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION,
    description = "Gold lanterns can be combined with dye to make colored lanterns.")
public class ColoredLanterns extends MesonModule {
    public static Map<ColorVariant, BaseLanternBlock> lanterns = new HashMap<>();

    @Override
    public void init() {
        for (ColorVariant color : ColorVariant.values()) {
            lanterns.put(color, new ColoredLanternBlock(this, color));
        }
    }

    @Override
    public void onClientSetup(FMLClientSetupEvent event) {
        lanterns.forEach((dyeColor, block) -> RenderTypeLookup.setRenderLayer(block, RenderType.getCutout()));
    }
}
