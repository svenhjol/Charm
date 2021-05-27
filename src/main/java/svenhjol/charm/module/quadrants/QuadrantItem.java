package svenhjol.charm.module.quadrants;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.item.CharmItem;

public class QuadrantItem extends CharmItem {
    public QuadrantItem(CharmModule module) {
        super(module, "quadrant", new FabricItemSettings()
            .maxCount(1)
            .maxDamage(64)
            .group(ItemGroup.MISC));
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(Items.COPPER_INGOT) || super.canRepair(stack, ingredient);
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }
}
