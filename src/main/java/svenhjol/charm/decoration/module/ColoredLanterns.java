package svenhjol.charm.decoration.module;

import net.minecraft.item.DyeColor;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.decoration.block.BaseLanternBlock;
import svenhjol.charm.decoration.block.ColoredLanternBlock;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID, category = CharmCategories.DECORATION,
    description = "Gold lanterns can be combined with dye to make colored lanterns.", enabledByDefault = false)
public class ColoredLanterns extends MesonModule {
    public static Map<DyeColor, BaseLanternBlock> lanterns = new HashMap<>();

    @Override
    public void init() {
        for (DyeColor color : DyeColor.values()) {
            lanterns.put(color, new ColoredLanternBlock(this, color));
        }
    }
}
