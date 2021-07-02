package svenhjol.charm.module.quadrants;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.item.CharmItem;

public class QuadrantItem extends CharmItem {
    public QuadrantItem(CharmModule module) {
        super(module, "quadrant", new FabricItemSettings()
            .stacksTo(1)
            .durability(64)
            .tab(CreativeModeTab.TAB_MISC));
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return ingredient.is(Items.COPPER_INGOT) || super.isValidRepairItem(stack, ingredient);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
