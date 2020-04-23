package svenhjol.charm.smithing.module;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmCategories;
import svenhjol.charm.tools.item.BoundCompassItem;
import svenhjol.charm.tools.module.CompassBinding;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Config;
import svenhjol.meson.iface.Module;

@Module(mod = Charm.MOD_ID, category = CharmCategories.SMITHING, hasSubscriptions = true,
    description = "Add an Eye of Ender to a Bound Compass on an anvil to fix its position across dimensions.\n" +
        "This requires the Compass Binding module to be enabled.")
public class DimensionalCompass extends MesonModule {
    @Config(name = "XP cost", description = "XP cost of combining an eye of ender with the bound compass.")
    public static int xpCost = 1;

    @Override
    public boolean shouldRunSetup() {
        return Meson.isModuleEnabled("charm:compass_binding");
    }

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();
        ItemStack out;

        if (left.isEmpty() || right.isEmpty()) return;
        if (left.getItem() != CompassBinding.item) return;
        if (right.getItem() != Items.ENDER_EYE) return;

        out = left.copy();

        BoundCompassItem.setDimensional(out, true);

        event.setCost(xpCost);
        event.setMaterialCost(1);
        event.setOutput(out);
    }
}
