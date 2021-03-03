package svenhjol.charm.module;

import net.minecraft.util.DyeColor;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.item.ColoredBundleItem;

import java.util.HashMap;
import java.util.Map;

@Module(mod = Charm.MOD_ID)
public class ColoredBundles extends CharmModule {
    public static final Map<DyeColor, ColoredBundleItem> COLORED_BUNDLES = new HashMap<>();

    @Override
    public void register() {
        for (DyeColor color : DyeColor.values()) {
            COLORED_BUNDLES.put(color, new ColoredBundleItem(this, color));
        }
    }
}
